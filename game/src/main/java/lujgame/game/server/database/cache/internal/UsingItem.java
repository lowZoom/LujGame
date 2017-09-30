package lujgame.game.server.database.cache.internal;

import lujgame.game.server.database.cache.message.DbCacheUseItem;

public class UsingItem {

  public UsingItem(DbCacheUseItem useItem, CacheItem cacheItem) {
    _useItem = useItem;
    _cacheItem = cacheItem;
  }

  public DbCacheUseItem getUseItem() {
    return _useItem;
  }

  public CacheItem getCacheItem() {
    return _cacheItem;
  }

  private final DbCacheUseItem _useItem;
  private final CacheItem _cacheItem;
}
