package lujgame.game.server.database.cache.internal;

import lujgame.game.server.database.cache.message.DbCacheUseItem;

public class UsingItem {

  public UsingItem(String cacheKey, DbCacheUseItem useItem,
      CacheItem cacheItem) {
    _cacheKey = cacheKey;

    _useItem = useItem;
    _cacheItem = cacheItem;
  }

  public DbCacheUseItem getUseItem() {
    return _useItem;
  }

  public CacheItem getCacheItem() {
    return _cacheItem;
  }

  @Deprecated
  private final String _cacheKey;

  private final DbCacheUseItem _useItem;
  private final CacheItem _cacheItem;
}
