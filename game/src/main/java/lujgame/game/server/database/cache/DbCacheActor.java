package lujgame.game.server.database.cache;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.LoggingAdapter;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableSet;
import com.typesafe.config.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import lujgame.core.akka.common.CaseActor;
import lujgame.game.boot.message.BootFailMsg;
import lujgame.game.server.database.cache.internal.CacheItem;
import lujgame.game.server.database.cache.internal.DbCacheUser;
import lujgame.game.server.database.cache.internal.UsingItem;
import lujgame.game.server.database.cache.message.DbCacheUseItem;
import lujgame.game.server.database.cache.message.DbCacheUseReq;
import lujgame.game.server.database.cache.message.DbCacheUseRsp;
import lujgame.game.server.database.load.DbLoadActorFactory;
import lujgame.game.server.database.load.message.DbLoadSetReq;
import lujgame.game.server.database.load.message.DbLoadSetRsp;

public class DbCacheActor extends CaseActor {

  public DbCacheActor(
      DbCacheActorState state,
      DbLoadActorFactory dbLoadActorFactory,
      DbCacheUser dbCacheUser) {
    _state = state;

    _dbLoadActorFactory = dbLoadActorFactory;
    _dbCacheUser = dbCacheUser;

    addCase(DbCacheUseReq.class, this::onDbCacheUse);
    addCase(DbLoadSetRsp.class, this::onLoadSetRsp);
  }

  @Override
  public void preStart() {
    DbCacheActorState state = _state;
    LoggingAdapter log = log();

    try {
      log.debug("检测数据库...");
      HikariDataSource dataSrc = initDatabase(state.getDatabaseConfig());

      startLoadActor(dataSrc);

    } catch (SQLException | RuntimeException e) {
      ActorRef rootActor = getContext().parent();
      rootActor.tell(new BootFailMsg(e.getMessage()), getSelf());
      return;
    }

    _state.setCache(CacheBuilder.newBuilder()
        .maximumSize(65536)
        .expireAfterAccess(90, TimeUnit.MINUTES)
        .expireAfterWrite(90, TimeUnit.MINUTES)
        .build(new CacheLoader<String, CacheItem>() {
          @Override
          public CacheItem load(@Nonnull String key) throws Exception {
            return loadCache(key);
          }
        }));
  }

  private HikariDataSource initDatabase(Config dbCfg) throws SQLException {
    HikariConfig cfg = new HikariConfig();
    cfg.setJdbcUrl(dbCfg.getString("url"));

    HikariDataSource dataSource = new HikariDataSource(cfg);
    Connection conn = dataSource.getConnection();

    log().debug("数据库连通性：" + conn.isValid(3));

    //TODO: 顺便检查所有的表都在不在

    conn.close();

    return dataSource;
  }

  private void startLoadActor(HikariDataSource dataSource) {
    Props props = _dbLoadActorFactory.props(dataSource);

    ActorRef loadRef = getContext().actorOf(props);
    _state.setLoaderRef(loadRef);
  }

  private void onDbCacheUse(DbCacheUseReq msg) {
    List<DbCacheUseItem> useList = msg.getUseList();
    log().debug("进行缓存读取，数量：{}", useList.size());

    LoadingCache<String, CacheItem> cache = _state.getCache();

    //1. 先转换所有item，触发预读
    //2. 看看有没有不可用需要等待的，有就中断
    //3. 没有需要等待的话，锁住所有项，回调

    DbCacheUser u = _dbCacheUser;

    List<UsingItem> waitList = useList.stream()
        .map(i -> makeUsingItem(i, cache))
//        .filter(i -> !u.isAvailable(i.getCacheItem()))
        .collect(Collectors.toList());

    if (!waitList.isEmpty()) {
      u.waitCache(_state, msg);
      return;
    }

    //TODO: 锁住对应缓存项
    //TODO: 回调到处理器上
  }

  private void onLoadSetRsp(DbLoadSetRsp msg) {
    String cacheKey = msg.getCacheKey();
    log().debug("数据库读取完成：{}", cacheKey);

    LoadingCache<String, CacheItem> cache = _state.getCache();
    try {
      CacheItem item = cache.get(cacheKey);

      ImmutableSet<Long> resultSet = msg.getResultSet();
      item.setValue(resultSet);

      item.setLoadOk(true);

      //TODO: 唤醒等待队列
      //TODO: 按顺序遍历队列，等第一个使用者，超时丢弃

      LinkedList<DbCacheUseReq> waitQueue = _state.getWaitQueue();
      Iterator<DbCacheUseReq> iter = waitQueue.iterator();

      while (iter.hasNext()) {
        DbCacheUseReq req = iter.next();

        if (isRelated(req, cacheKey)) {
          if (tryFinishCacheReq(req)) {
            log().debug("触发数据库CMD：{}", req.getCmdType());
            iter.remove();
          }

          // 这个用了就锁住了，后面的就不用再看了
          break;
        }
      }

    } catch (ExecutionException e) {
      e.printStackTrace();
    }
  }

  /**
   * @param req 判断此次读取是否与该请求有关
   */
  private boolean isRelated(DbCacheUseReq req, String cacheKey) {
    return req.getUseList().stream().anyMatch(i -> Objects.equals(i.getCacheKey(), cacheKey));
  }

  private boolean tryFinishCacheReq(DbCacheUseReq req) {
    LoadingCache<String, CacheItem> cache = _state.getCache();

    List<UsingItem> useList = req.getUseList().stream()
        .map(i -> makeUsingItem(i, cache)) // 每次都要重新取，缓存都会有
        .collect(Collectors.toList());

    // 如果还有没就绪的缓存，则无法完成此请求
    if (useList.stream().anyMatch(i -> !_dbCacheUser.isAvailable(i.getCacheItem()))) {
      return false;
    }

    Builder<String, CacheItem> builder = ImmutableMap.builder();

    // 锁住对应缓存项
    for (UsingItem item : useList) {
      CacheItem ci = item.getCacheItem();
      ci.setLock(true);

      String resultKey = item.getUseItem().getResultKey();
      builder.put(resultKey, ci);

      //TODO: 处理缓存被锁定后刚好过期的情况，可以把锁住的缓存另用一个map存住，用完再设回cache
    }

    //TODO: 调用对应CMD
    ActorRef requestRef = req.getRequestRef();
    requestRef.tell(new DbCacheUseRsp(builder.build(), req.getCmdType(),
        req.getRequestTime()), requestRef);

    return true;
  }

  private UsingItem makeUsingItem(DbCacheUseItem useItem, LoadingCache<String, CacheItem> cache) {
    String cacheKey = useItem.getCacheKey();
    CacheItem cacheItem = _dbCacheUser.getCacheItem(cache, cacheKey);
    return new UsingItem(cacheKey, useItem, cacheItem);
  }

  private CacheItem loadCache(String cacheKey) {
    ActorRef loadRef = _state.getLoaderRef();
    DbLoadSetReq req = new DbLoadSetReq(cacheKey);
    loadRef.tell(req, getSelf());

    return new CacheItem();
  }

  private final DbCacheActorState _state;

  private final DbLoadActorFactory _dbLoadActorFactory;
  private final DbCacheUser _dbCacheUser;
}
