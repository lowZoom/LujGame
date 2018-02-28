package lujgame.game.server.database.cache.internal;

import com.google.common.cache.Cache;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import lujgame.core.spring.inject.LujInternal;

@LujInternal
public class DbCacheReturner {

  public void returnCache(Set<CacheItem> oldItemList,
      Map<String, CacheItem> lockMap, Cache<String, CacheItem> cache) {
    //TODO: 丢弃删除的item

    //TODO: 把原有item的lock置为false
    for (CacheItem item : oldItemList) {
      _dbCacheLocker.unlockItem(item, lockMap, cache);
    }

    //TODO: 添加新创建的item

  }

  @Inject
  private DbCacheLocker _dbCacheLocker;
}
