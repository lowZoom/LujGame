package lujgame.game.server.database.cache.internal;

import static com.google.common.base.Preconditions.checkState;

import com.google.common.cache.Cache;
import java.util.Map;
import java.util.Objects;
import lujgame.game.server.core.LujInternal;

@LujInternal
public class DbCacheLocker {

  public void lockItem(Map<String, CacheItem> lockMap, CacheItem item) {
    String cacheKey = item.getCacheKey();
    checkState(item.isLoadOk(), cacheKey);
    checkState(!item.isLock(), "重复上锁：%s", cacheKey);

    item.setLock(true);
    CacheItem old = lockMap.put(cacheKey, item);

    checkState(old == null, "重复上锁：%s", cacheKey);
  }

  public void unlockItem(CacheItem item, Map<String, CacheItem> lockMap,
      Cache<String, CacheItem> cache) {
    String cacheKey = item.getCacheKey();
    checkState(item.isLoadOk(), cacheKey);
    checkState(item.isLock(), "重复解锁：%s", cacheKey);

    CacheItem rmItem = lockMap.remove(cacheKey);
    checkState(Objects.equals(rmItem, item), cacheKey);

    rmItem.setLock(false);
    cache.put(cacheKey, item);
  }
}
