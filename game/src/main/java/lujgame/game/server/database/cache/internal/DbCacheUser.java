package lujgame.game.server.database.cache.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import akka.actor.ActorRef;
import com.google.common.cache.Cache;
import com.google.common.cache.LoadingCache;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import javax.inject.Inject;
import lujgame.core.akka.AkkaTool;
import lujgame.game.server.database.load.message.DbLoadObjReq;
import lujgame.game.server.database.type.DbId;
import lujgame.game.server.database.type.IdSet;
import org.springframework.stereotype.Component;

@Component
public class DbCacheUser {

  public CacheItem finishLoadItem(Cache<String, CacheItem> cache,
      String cacheKey, Object resultValue) {
    CacheItem item = checkNotNull(cache.getIfPresent(cacheKey), cacheKey);

    checkState(item.getValue() == null, cacheKey);
    checkState(!item.isLoadOk(), cacheKey);
    checkState(!item.isLock());

    item.setValue(checkNotNull(resultValue, cacheKey));
    item.setLoadOk(true);

    return item;
  }

  public boolean useCacheSet(LoadingCache<String, CacheItem> cache,
      String cacheKey) throws ExecutionException {
    //TODO: 先判断idSet在缓存没有，没有发起读取，有就拿出来遍历，发起所有对象的读取

    CacheItem cacheItem = cache.get(cacheKey);

    if (!isAvailable(cacheItem)) {
      return false;
    }

    //TODO: 要先转换成set

    IdSet idSet = (IdSet) cacheItem.getValue();
    boolean ok = true;

    // 要让每一个id都跑到，确定会发起必要的读取
    for (DbId dbId : idSet.iter()) {
      ok = useCacheObj(cache, dbId) && ok;
    }

    return ok;
  }

  public boolean useCacheObj(LoadingCache<String, CacheItem> cache,
      DbId dbId) throws ExecutionException {
    String cacheKey = null;
    CacheItem cacheItem = cache.get(cacheKey);

    if (!isAvailable(cacheItem)) {
      return false;
    }

    //TODO: 转换成对象进行处理

    return true;
  }

  public void lockAndCallback() {
  }

  public boolean prepareSet(Cache<String, CacheItem> cache,
      CacheItem cacheItem, ActorRef cacheRef, ActorRef loaderRef) {
    if (!isAvailable(cacheItem)) {
      return false;
    }

    Set<Long> idSet = (Set<Long>) cacheItem.getValue();
    Class<?> dbType = cacheItem.getDbType();

    for (Long id : idSet) {
      String cacheKey = _cacheKeyMaker.makeObjectKey(dbType, id);
      CacheItem elemItem = cache.getIfPresent(cacheKey);

      if (elemItem == null) {
        elemItem = requestLoadObj(cache, cacheKey, dbType, cacheRef, loaderRef);
      }

      if (!isAvailable(elemItem)) {
        return false;
      }
    }

    return true;
  }

  public boolean isAvailable(CacheItem cacheItem) {
    return cacheItem.isLoadOk() && !cacheItem.isLock();
  }

  private CacheItem requestLoadObj(Cache<String, CacheItem> cache,
      String cacheKey, Class<?> dbType, ActorRef cacheRef, ActorRef loaderRef) {
    CacheItem item = new CacheItem(cacheKey, dbType);
    cache.put(cacheKey, item);

    _akkaTool.tell(new DbLoadObjReq(cacheKey), cacheRef, loaderRef);
    return item;
  }

  @Inject
  private AkkaTool _akkaTool;

  @Inject
  private CacheKeyMaker _cacheKeyMaker;
}
