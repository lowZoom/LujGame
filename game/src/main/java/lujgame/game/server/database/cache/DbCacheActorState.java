package lujgame.game.server.database.cache;

import akka.actor.ActorRef;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.typesafe.config.Config;
import lujgame.game.server.database.cache.internal.CacheItem;
import lujgame.game.server.database.cache.message.DbCacheUseReq;

public class DbCacheActorState {

  public DbCacheActorState(Config databaseConfig) {
    _databaseConfig = databaseConfig;

    _waitingMap = ArrayListMultimap.create();
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

  public Multimap<String, DbCacheUseReq> getWaitingMap() {
    return _waitingMap;
  }

  public Config getDatabaseConfig() {
    return _databaseConfig;
  }

  private LoadingCache<String, CacheItem> _cache;

  private ActorRef _loaderRef;

  /**
   * 等待数据可用的请求队列，key->缓存键值
   */
  private final Multimap<String, DbCacheUseReq> _waitingMap;

  private final Config _databaseConfig;
}
