package lujgame.game.server.database.cache;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.LoggingAdapter;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.typesafe.config.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
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
import lujgame.game.server.database.load.DbLoadActorFactory;
import lujgame.game.server.database.load.message.DbLoadSetReq;

public class DbCacheActor extends CaseActor {

  public DbCacheActor(
      DbCacheActorState state,
      DbLoadActorFactory dbLoadActorFactory,
      DbCacheUser dbCacheUser) {
    _state = state;

    _dbLoadActorFactory = dbLoadActorFactory;
    _dbCacheUser = dbCacheUser;

    addCase(DbCacheUseReq.class, this::onDbCacheUse);
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
        .filter(i -> !u.isAvailable(i.getCacheItem()))
        .collect(Collectors.toList());

    if (!waitList.isEmpty()) {
      u.waitCache(_state, msg);
      return;
    }

    //TODO: 锁住对应缓存项


    //TODO: 回调到处理器上

  }

  private UsingItem makeUsingItem(DbCacheUseItem useItem,
      LoadingCache<String, CacheItem> cache) {
    DbCacheUser u = _dbCacheUser;

    String cacheKey = u.makeCacheKey(useItem.getDbType());
    CacheItem cacheItem = u.getCacheItem(cache, cacheKey);

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
