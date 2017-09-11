package lujgame.game.server.database.cache;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Multimap;
import com.typesafe.config.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import lujgame.core.akka.common.CaseActor;
import lujgame.game.boot.message.BootFailMsg;
import lujgame.game.server.database.cache.message.DbCacheUseReq;
import lujgame.game.server.database.cache.message.UseItem;

public class DbCacheActor extends CaseActor {

  public DbCacheActor(DbCacheActorState state) {
    _state = state;

    addCase(DbCacheUseReq.class, this::onDbCacheUse);
  }

  @Override
  public void preStart() {
    DbCacheActorState state = _state;
    LoggingAdapter log = log();

    try {
      log.debug("检测数据库...");
      checkDatabaseConnectivity(state.getDatabaseConfig());

      //TODO: 检查通过的话，就启动LoadActor

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

  private void checkDatabaseConnectivity(Config dbCfg) throws SQLException {
    HikariConfig cfg = new HikariConfig();
    cfg.setJdbcUrl(dbCfg.getString("url"));

    HikariDataSource dataSource = new HikariDataSource(cfg);
    Connection conn = dataSource.getConnection();

    System.out.println("校验数据库连通性：" + conn.isValid(3));

    //TODO: 顺便检查所有的表都在不在

    conn.close();
  }

  private void onDbCacheUse(DbCacheUseReq msg) {
    List<UseItem> useList = msg.getUseList();
    log().debug("进行缓存读取，数量：{}", useList.size());

    LoadingCache<String, CacheItem> cache = _state.getCache();

    for (UseItem item : useList) {
      String cacheKey = makeCacheKey(item.getDbType());
      if (isAvailable(cache, cacheKey)) {
        continue;
      }

      //TODO: 没读取到内存的发起读取

      Multimap<String, UseItem> waitingMap = _state.getWaitingMap();
      waitingMap.put(cacheKey, item);

      // 若非万事俱备，则中断
      return;
    }

    //TODO: 锁住对应缓存项

    //TODO: 回调到处理器上

  }

  private String makeCacheKey(Class<?> dbType) {
    return dbType.getSimpleName();
  }

  private boolean isAvailable(LoadingCache<String, CacheItem> cache, String cacheKey) {
    try {
      CacheItem item = cache.get(cacheKey);
      return item._loadOk && !item._lock;
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  private CacheItem loadCache(String cacheKey) {
    //TODO: 发起异步读取

    return new CacheItem();
  }

  private final DbCacheActorState _state;
}
