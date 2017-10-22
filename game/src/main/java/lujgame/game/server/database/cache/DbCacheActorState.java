package lujgame.game.server.database.cache;

import akka.actor.ActorRef;
import com.google.common.cache.Cache;
import com.typesafe.config.Config;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import lujgame.game.server.database.cache.internal.CacheItem;
import lujgame.game.server.database.cache.message.DbCacheUseReq;

public class DbCacheActorState {

  public DbCacheActorState(Config databaseConfig) {
    _databaseConfig = databaseConfig;

    _lockMap = new HashMap<>(256);
    _waitQueue = new LinkedList<>();
  }

  public Cache<String, CacheItem> getCache() {
    return _cache;
  }

  public void setCache(Cache<String, CacheItem> cache) {
    _cache = cache;
  }

  public Map<String, CacheItem> getLockMap() {
    return _lockMap;
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

  private Cache<String, CacheItem> _cache;
  private ActorRef _loaderRef;

  /**
   * 保存锁定的缓存项，以防缓存在锁定过程中被淘汰
   * 应先到这里查询是否被锁，找不到再到cache里找
   */
  private final Map<String, CacheItem> _lockMap;

  private final LinkedList<DbCacheUseReq> _waitQueue;
  private final Config _databaseConfig;
}
