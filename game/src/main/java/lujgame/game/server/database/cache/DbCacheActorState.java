package lujgame.game.server.database.cache;

import com.google.common.cache.LoadingCache;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.typesafe.config.Config;
import lujgame.game.server.database.cache.message.UseItem;

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

  public Multimap<String, UseItem> getWaitingMap() {
    return _waitingMap;
  }

  public Config getDatabaseConfig() {
    return _databaseConfig;
  }

  private LoadingCache<String, CacheItem> _cache;

  /**
   * 等待数据可用的请求
   */
  private final Multimap<String, UseItem> _waitingMap;

  private final Config _databaseConfig;
}
