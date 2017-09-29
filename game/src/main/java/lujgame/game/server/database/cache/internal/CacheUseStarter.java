package lujgame.game.server.database.cache.internal;

import static com.google.common.base.Preconditions.checkState;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import com.google.common.cache.Cache;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lujgame.core.akka.AkkaTool;
import lujgame.game.server.database.cache.DbCacheActorState;
import lujgame.game.server.database.cache.message.DbCacheUseItem;
import lujgame.game.server.database.cache.message.DbCacheUseReq;
import lujgame.game.server.database.load.message.DbLoadObjReq;
import lujgame.game.server.database.load.message.DbLoadSetReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CacheUseStarter {

  public void startUseObject(DbCacheActorState state,
      DbCacheUseReq msg, ActorRef cacheRef, LoggingAdapter log) {
    //TODO: 分开处理 对象读取 和 集合读取

    List<DbCacheUseItem> setUseList = msg.getSetUseList();

//    List<DbCacheUseItem> useList = null;// msg.getUseList();
    log.debug("进行缓存读取，数量：{}", setUseList.size());

    Cache<String, CacheItem> cache = state.getCache();

    //1. 先转换所有item，触发预读
    //2. 看看有没有不可用需要等待的，有就中断
    //3. 没有需要等待的话，锁住所有项，回调

    ActorRef loaderRef = state.getLoaderRef();

    List<UsingItem> setUsingList = setUseList.stream()
        .map(i -> makeUsingItem(i, cache, cacheRef, loaderRef))
        .collect(Collectors.toList());

    long waitCount = setUsingList.stream()
        .filter(i -> !prepareSet(cache, i, cacheRef, loaderRef))
        .count();

    //TODO: 还要考虑集合内元素的情况

    if (waitCount > 0) {
      waitCache(state, msg);
      return;
    }

    // 锁住对应缓存项
    setUsingList.forEach(this::lockItem);

    //TODO: 回调到处理器上
    log.debug("未做：回调到处理器上");
  }

  private UsingItem makeUsingItem(DbCacheUseItem useItem,
      Cache<String, CacheItem> cache, ActorRef cacheRef, ActorRef loaderRef) {
    String cacheKey = useItem.getCacheKey();

    CacheItem cacheItem = cache.getIfPresent(cacheKey);
    if (cacheItem == null) {
      cacheItem = requestLoadSet(cache, cacheKey, cacheRef, loaderRef);
    }

    checkState(!cacheItem.isLock() || cacheItem.isLock() == cacheItem.isLoadOk(),
        "没读取完成居然就被锁住了，cacheKey: %s", cacheKey);

    checkState(!cacheItem.isLoadOk() || cacheItem.getValue() != null,
        "读取完成但居然没读取结果，cacheKey: %s", cacheKey);

    return new UsingItem(cacheKey, useItem, cacheItem);
  }

  private CacheItem requestLoadSet(Cache<String, CacheItem> cache,
      String cacheKey, ActorRef cacheRef, ActorRef loaderRef) {
    CacheItem item = new CacheItem();
    cache.put(cacheKey, item);

    _akkaTool.tell(new DbLoadSetReq(cacheKey), cacheRef, loaderRef);
    return item;
  }

  private boolean prepareSet(Cache<String, CacheItem> cache,
      UsingItem item, ActorRef cacheRef, ActorRef loaderRef) {
    DbCacheUser u = _dbCacheUser;

    CacheItem cacheItem = item.getCacheItem();
    if (!u.isAvailable(cacheItem)) {
      return false;
    }

    Set<Long> idSet = (Set<Long>) cacheItem.getValue();
    Class<?> dbType = item.getUseItem().getDbType();

    for (Long id : idSet) {
      String cacheKey = _cacheKeyMaker.makeObjectKey(dbType, id);
      CacheItem elemItem = cache.getIfPresent(cacheKey);

      if (elemItem == null) {
        elemItem = requestLoadObj(cache, cacheKey, cacheRef, loaderRef);
      }

      if (!u.isAvailable(elemItem)) {
        return false;
      }
    }

    return true;
  }

  private CacheItem requestLoadObj(Cache<String, CacheItem> cache,
      String cacheKey, ActorRef cacheRef, ActorRef loaderRef) {
    CacheItem item = new CacheItem();
    cache.put(cacheKey, item);

    _akkaTool.tell(new DbLoadObjReq(cacheKey), cacheRef, loaderRef);
    return item;
  }

  private void waitCache(DbCacheActorState state, DbCacheUseReq msg) {
    LinkedList<DbCacheUseReq> waitQueue = state.getWaitQueue();
    waitQueue.addLast(msg);
  }

  private void lockItem(UsingItem item) {
    item.getCacheItem().setLock(true);
  }

  @Autowired
  private AkkaTool _akkaTool;

  @Autowired
  private DbCacheUser _dbCacheUser;

  @Autowired
  private CacheKeyMaker _cacheKeyMaker;
}
