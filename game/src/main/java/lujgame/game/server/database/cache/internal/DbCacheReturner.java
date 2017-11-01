package lujgame.game.server.database.cache.internal;

import static com.google.common.base.Preconditions.checkState;

import com.google.common.cache.Cache;
import com.google.common.collect.ImmutableSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import lujgame.game.server.core.LujInternal;

@LujInternal
public class DbCacheReturner {

  public void returnCache(Set<CacheItem> oldItemList,
      Map<String, CacheItem> lockMap, Cache<String, CacheItem> cache) {
    //TODO: 丢弃删除的item

    //TODO: 把原有item的lock置为false
    for (CacheItem item : oldItemList) {
      returnItem(item, lockMap, cache);
    }

    //TODO: 添加新创建的item

  }

  private void returnItem(CacheItem item, Map<String, CacheItem> lockMap,
      Cache<String, CacheItem> cache) {
    String cacheKey = item.getCacheKey();
    checkState(item.isLoadOk(), cacheKey);
    checkState(item.isLock(), cacheKey);

    CacheItem rmItem = lockMap.remove(cacheKey);
    checkState(Objects.equals(rmItem, item), cacheKey);

    rmItem.setLock(false);
    cache.put(cacheKey, item);
  }
}
