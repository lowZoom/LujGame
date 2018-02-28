package lujgame.game.server.entity.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import akka.actor.ActorRef;
import com.google.common.cache.Cache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import lujgame.core.akka.AkkaTool;
import lujgame.core.spring.inject.LujInternal;
import lujgame.game.server.database.cache.internal.CacheItem;
import lujgame.game.server.database.cache.internal.CacheKeyMaker;
import lujgame.game.server.database.cache.internal.DbCacheLocker;
import lujgame.game.server.database.cache.internal.UsingItem;
import lujgame.game.server.database.cache.message.DbCacheUseReq;
import lujgame.game.server.database.cache.message.DbCacheUseRsp;
import lujgame.game.server.database.type.DbSetTool;
import lujgame.game.server.type.JSet;

@LujInternal
public class DbCmdExeStarter {

  /**
   * 执行CMD前的启动工作，在Cache里锁定对应的缓存，并发送到Entity触发执行
   */
  public void startExecute(Cache<String, CacheItem> cache, Map<String, CacheItem> lockMap,
      List<UsingItem> setUseList, DbCacheUseReq req, ActorRef cacheRef) {
    ImmutableMap.Builder<String, Object> result = ImmutableMap.builder();
    ImmutableSet.Builder<CacheItem> borrowItems = ImmutableSet.builder();

    // 锁住对应缓存项
    for (UsingItem item : setUseList) {
      CacheItem ci = item.getCacheItem();
      borrowCacheItem(lockMap, ci, borrowItems);

      String resultKey = item.getUseItem().getResultKey();
      JSet<?> resultSet = _dbSetTool.newDbSet(ci, useElem(cache, lockMap, ci, borrowItems));

      result.put(resultKey, resultSet);
    }

    _akkaTool.tell(new DbCacheUseRsp(req.getCmdType(), req.getParamMap(), result.build(),
        borrowItems.build(), req.getRequestTime()), cacheRef, req.getRequestRef());
  }

  private ImmutableList<CacheItem> useElem(Cache<String, CacheItem> cache,
      Map<String, CacheItem> lockMap, CacheItem setItem,
      ImmutableSet.Builder<CacheItem> borrowItems) {
    //TODO: 读取元素对应缓存，并进行锁定
    ImmutableList.Builder<CacheItem> result = ImmutableList.builder();

    Set<Long> idSet = _dbSetTool.getIdSet(setItem);
    Class<?> dbType = setItem.getDbType();

    for (Long dbId : idSet) {
      String key = _cacheKeyMaker.makeObjectKey(dbType, dbId);
      CacheItem elemItem = checkAndGet(cache, key);

      borrowCacheItem(lockMap, elemItem, borrowItems);
      result.add(elemItem);
    }

    return result.build();
  }

  private CacheItem checkAndGet(Cache<String, CacheItem> cache, String key) {
    return checkNotNull(cache.getIfPresent(key), key);
  }

  private void borrowCacheItem(Map<String, CacheItem> lockMap, CacheItem item,
      ImmutableSet.Builder<CacheItem> borrowItems) {
    _dbCacheLocker.lockItem(lockMap, item);
    borrowItems.add(item);
  }

  @Inject
  private AkkaTool _akkaTool;

  @Inject
  private CacheKeyMaker _cacheKeyMaker;

  @Inject
  private DbCacheLocker _dbCacheLocker;

  @Inject
  private DbSetTool _dbSetTool;
}
