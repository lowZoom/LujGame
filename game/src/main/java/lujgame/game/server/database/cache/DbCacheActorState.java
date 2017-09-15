package lujgame.game.server.database.cache;

import akka.actor.ActorRef;
import com.google.common.cache.LoadingCache;
import com.typesafe.config.Config;
import java.util.LinkedList;
import java.util.Map;
import lujgame.game.server.database.cache.internal.CacheItem;
import lujgame.game.server.database.cache.message.DbCacheUseReq;

public class DbCacheActorState {

  public DbCacheActorState(Config databaseConfig) {
    _databaseConfig = databaseConfig;

    _waitQueue = new LinkedList<>();
  }

  public LoadingCache<String, CacheItem> getCache() {
    return _cache;
  }

  public void setCache(LoadingCache<String, CacheItem> cache) {
    _cache = cache;
  }

  public ActorRef getLoaderRef() {
    return _loaderRef;
  }

  public void setLoaderRef(ActorRef loaderRef) {
    _loaderRef = loaderRef;
  }

  public LinkedList<DbCacheUseReq> getWaitQueue() {
    return _waitQueue;
  }

  public Config getDatabaseConfig() {
    return _databaseConfig;
  }

  private LoadingCache<String, CacheItem> _cache;

  //TODO: 保存正在锁定使用的缓存项，先到这里查询是否被锁，找不到再到cache里找
  private Map<String, CacheItem> _lockMap;

  private ActorRef _loaderRef;

  private final LinkedList<DbCacheUseReq> _waitQueue;

  private final Config _databaseConfig;
}
