package lujgame.game.server.database.cache.internal;

import static com.google.common.base.Preconditions.checkState;

import java.util.Map;
import lujgame.game.server.core.LujInternal;

@LujInternal
public class DbCacheLocker {

  public void lockItem(Map<String, CacheItem> lockMap, CacheItem item) {
    String key = item.getCacheKey();

    checkState(!item.isLock(), "重复上锁：%s", key);
    item.setLock(true);

    CacheItem old = lockMap.put(key, item);
    checkState(old == null, "重复上锁：%s", key);
  }
}
