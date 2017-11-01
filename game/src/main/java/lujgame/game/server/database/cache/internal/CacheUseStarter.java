package lujgame.game.server.database.cache.internal;

import static com.google.common.base.Preconditions.checkState;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import com.google.common.cache.Cache;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lujgame.core.akka.AkkaTool;
import lujgame.game.server.database.cache.DbCacheActorState;
import lujgame.game.server.database.cache.message.DbCacheUseItem;
import lujgame.game.server.database.cache.message.DbCacheUseReq;
import lujgame.game.server.database.load.message.DbLoadSetReq;
import lujgame.game.server.entity.internal.DbCmdExeStarter;
import org.springframework.stereotype.Component;

@Component
public class CacheUseStarter {

  /**
   * 在Cache上开始一次数据使用，如果数据全部就绪，直接回复给Entity
   *
   * FIXME: 名字起得有问题，use的不只object
   */
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
        .filter(i -> !_dbCacheUser.prepareSet(cache, i.getCacheItem(), cacheRef, loaderRef))
        .count();

    //TODO: 还要考虑集合内元素的情况

    if (waitCount > 0) {
      waitCache(state, msg);
      return;
    }

    // 回调到处理器上
    _dbCmdExeStarter.startExecute(cache, state.getLockMap(), setUsingList, msg, cacheRef);
  }

  private UsingItem makeUsingItem(DbCacheUseItem useItem,
      Cache<String, CacheItem> cache, ActorRef cacheRef, ActorRef loaderRef) {
    String cacheKey = useItem.getCacheKey();

    CacheItem cacheItem = cache.getIfPresent(cacheKey);
    if (cacheItem == null) {
      cacheItem = requestLoadSet(cache, cacheKey, useItem.getDbType(), cacheRef, loaderRef);
    }

    checkState(!cacheItem.isLock() || cacheItem.isLock() == cacheItem.isLoadOk(),
        "没读取完成居然就被锁住了，cacheKey: %s", cacheKey);

    checkState(!cacheItem.isLoadOk() || cacheItem.getValue() != null,
        "读取完成但居然没读取结果，cacheKey: %s", cacheKey);

    return new UsingItem(useItem, cacheItem);
  }

  private CacheItem requestLoadSet(Cache<String, CacheItem> cache,
      String cacheKey, Class<?> dbType, ActorRef cacheRef, ActorRef loaderRef) {
    CacheItem item = new CacheItem(cacheKey, dbType);
    cache.put(cacheKey, item);

    _akkaTool.tell(new DbLoadSetReq(cacheKey), cacheRef, loaderRef);
    return item;
  }

  private void waitCache(DbCacheActorState state, DbCacheUseReq msg) {
    LinkedList<DbCacheUseReq> waitQueue = state.getWaitQueue();
    waitQueue.addLast(msg);
  }

  @Inject
  private AkkaTool _akkaTool;

  @Inject
  private DbCacheUser _dbCacheUser;

  @Inject
  private DbCmdExeStarter _dbCmdExeStarter;
}
