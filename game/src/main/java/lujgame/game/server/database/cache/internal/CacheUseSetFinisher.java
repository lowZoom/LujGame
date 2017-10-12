package lujgame.game.server.database.cache.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import com.google.common.cache.Cache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lujgame.core.akka.AkkaTool;
import lujgame.game.server.database.cache.DbCacheActorState;
import lujgame.game.server.database.cache.message.DbCacheUseItem;
import lujgame.game.server.database.cache.message.DbCacheUseReq;
import lujgame.game.server.database.cache.message.DbCacheUseRsp;
import lujgame.game.server.database.load.message.DbLoadSetRsp;
import lujgame.game.server.database.type.DbSetTool;
import lujgame.game.server.type.JSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CacheUseSetFinisher {

  public void finishUseSet(DbCacheActorState state,
      DbLoadSetRsp msg, ActorRef cacheRef, LoggingAdapter log) {
    String cacheKey = msg.getCacheKey();
    log.debug("数据库读取完成：{}", cacheKey);

    Cache<String, CacheItem> cache = state.getCache();
    ImmutableSet<Long> resultSet = msg.getResultSet();

    DbCacheUser u = _dbCacheUser;
    CacheItem cacheItem = u.finishLoadItem(cache, msg.getCacheKey(), resultSet);

    // 发起必要的读取请求
    if (!u.prepareSet(cache, cacheItem, cacheRef, state.getLoaderRef())) {
      return;
    }

    //TODO: 唤醒等待队列
    //TODO: 按顺序遍历队列，等第一个使用者，超时丢弃
    LinkedList<DbCacheUseReq> waitQueue = state.getWaitQueue();
    Iterator<DbCacheUseReq> iter = waitQueue.iterator();

    while (iter.hasNext()) {
      DbCacheUseReq req = iter.next();
      if (!isRelated(req, cacheKey)) {
        continue;
      }

      if (tryFinishCacheReq(state, req)) {
        log.debug("触发数据库CMD：{}", req.getCmdType());
        iter.remove();
      }

      // 这个用了就锁住了，后面的就不用再看了，应该有个锁定超时
      break;
    }
  }

  /**
   * @param req 判断此次读取是否与该请求有关
   */
  private boolean isRelated(DbCacheUseReq req, String cacheKey) {
    return req.getSetUseList().stream().anyMatch(i -> Objects.equals(i.getCacheKey(), cacheKey));
  }

  private boolean tryFinishCacheReq(DbCacheActorState state, DbCacheUseReq req) {
    Cache<String, CacheItem> cache = state.getCache();

    List<UsingItem> setUseList = req.getSetUseList().stream()
        .map(i -> makeUsingItem(i, cache)) // 每次都要重新取，缓存都会有
        .collect(Collectors.toList());

    // 如果还有未就绪的缓存，则无法完成此请求
    if (setUseList.stream().anyMatch(i -> !isSetAvailable(cache, i.getCacheItem()))) {
      return false;
    }

    ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();

    // 锁住对应缓存项
    for (UsingItem item : setUseList) {
      CacheItem ci = item.getCacheItem();
      ci.setLock(true);

      String resultKey = item.getUseItem().getResultKey();
      JSet<?> resultSet = new JSet<>(_dbSetTool.createImpl(ci, useElem(cache, ci)));

      builder.put(resultKey, resultSet);

      //TODO: 处理缓存被锁定后刚好过期的情况，可以把锁住的缓存另用一个map存住，用完再设回cache
    }

    //TODO: 调用对应CMD
    ActorRef requestRef = req.getRequestRef();
    _akkaTool.tellSelf(new DbCacheUseRsp(req.getCmdType(),
        req.getParamMap(), builder.build(), req.getRequestTime()), requestRef);

    return true;
  }

  private ImmutableList<CacheItem> useElem(Cache<String, CacheItem> cache, CacheItem setItem) {
    //TODO: 读取元素对应缓存，并进行锁定
    ImmutableList.Builder<CacheItem> result = ImmutableList.builder();

    Set<Long> idSet = _dbSetTool.getIdSet(setItem);
    Class<?> dbType = setItem.getDbType();

    for (Long dbId : idSet) {
      String key = _cacheKeyMaker.makeObjectKey(dbType, dbId);
      CacheItem elemItem = checkAndGet(cache, key);

      result.add(elemItem);
    }

    return result.build();
  }

  private static CacheItem checkAndGet(Cache<String, CacheItem> cache, String key) {
    return checkNotNull(cache.getIfPresent(key), key);
  }

  private UsingItem makeUsingItem(DbCacheUseItem useItem, Cache<String, CacheItem> cache) {
    String cacheKey = useItem.getCacheKey();
    CacheItem cacheItem = checkNotNull(cache.getIfPresent(cacheKey), cacheKey);

    checkState(!cacheItem.isLock() || cacheItem.isLock() == cacheItem.isLoadOk(),
        "没读取完成居然就被锁住了，cacheKey: %s", cacheKey);

    return new UsingItem(useItem, cacheItem);
  }

  private boolean isSetAvailable(Cache<String, CacheItem> cache, CacheItem item) {
    DbCacheUser u = _dbCacheUser;
    if (!u.isAvailable(item)) {
      return false;
    }

    Set<Long> idSet = _dbSetTool.getIdSet(item);
    Class<?> dbType = item.getDbType();

    for (Long id : idSet) {
      String cacheKey = _cacheKeyMaker.makeObjectKey(dbType, id);
      CacheItem elemItem = checkAndGet(cache, cacheKey);

      if (!u.isAvailable(elemItem)) {
        return false;
      }
    }

    return true;
  }

  @Autowired
  private AkkaTool _akkaTool;

  @Autowired
  private DbCacheUser _dbCacheUser;

  @Autowired
  private CacheKeyMaker _cacheKeyMaker;

  @Autowired
  private DbSetTool _dbSetTool;
}
