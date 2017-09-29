package lujgame.game.server.database.cache.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.cache.Cache;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import lujgame.game.server.database.cache.DbCacheActorState;
import lujgame.game.server.database.cache.message.DbCacheUseItem;
import lujgame.game.server.database.cache.message.DbCacheUseReq;
import lujgame.game.server.database.load.message.DbLoadObjRsp;
import lujgame.game.server.database.type.DbId;
import lujgame.game.server.database.type.IdSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CacheUseObjFinisher {

  @Autowired
  public CacheUseObjFinisher(DbCacheUser dbCacheUser) {
    _dbCacheUser = dbCacheUser;
  }

  public void finishUseObject(DbCacheActorState state, DbLoadObjRsp msg) {
    DbCacheUser u = _dbCacheUser;

    Cache<String, CacheItem> cache = state.getCache();
    String cacheKey = msg.getCacheKey();
    u.finishLoadItem(cache, cacheKey, msg.getResultObject());

    //TODO: 唤醒等待队列的时候，要考虑该数据类型的集合
    LinkedList<DbCacheUseReq> waitQueue = state.getWaitQueue();
    Iterator<DbCacheUseReq> iter = waitQueue.iterator();

    while (iter.hasNext()) {
      DbCacheUseReq req = iter.next();

      if (isRelated(cache, req, msg.getDbType())) {
        if (isReady(cache, req)) {
          u.lockAndCallback();
          iter.remove();
        }

        // 这个用了就锁住了，后面的就不用再看了，应该有个锁定超时
        break;
      }
    }
  }

  /**
   * @param req 判断读取完成的对象类型是否与该请求相关
   */
  private boolean isRelated(Cache<String, CacheItem> cache, DbCacheUseReq req, Class<?> dbType) {
    List<DbCacheUseItem> setUseList = req.getSetUseList();

    //TODO: 检查set对应的缓存项是否已读取完成，没读取完成的就无关

    for (DbCacheUseItem item : setUseList) {
      if (Objects.equals(item.getDbType(), dbType)) {
        return true;
      }
    }

    return false;
  }

  /**
   * @param req 该请求所需要的数据是否已经全部准备就绪
   */
  private boolean isReady(Cache<String, CacheItem> cache, DbCacheUseReq req) {
    //TODO: 遍历查找有没有相同类型的set，有的话看set读取好没，读取好的话看包不包含改对象，包含的话看全部对象好了没
//    req.getSetUseList().stream()
//        .filter(u::isSetRelated)
//        //TODO: 从obj信息找set
//        .map(i -> cache.getIfPresent(i.getCacheKey()))
//        .filter(i -> u.isAvailable(i.getcache))

    return req.getSetUseList().stream().allMatch(i -> isSetReady(cache, i));
  }

  private boolean isSetReady(Cache<String, CacheItem> cache, DbCacheUseItem setItem) {
    String cacheKey = setItem.getCacheKey();

    CacheItem item = getAndCheckItem(cache, cacheKey);
    if (!_dbCacheUser.isAvailable(item)) {
      return false;
    }

    IdSet idSet = (IdSet) checkNotNull(item.getValue(), cacheKey);
    Class<?> dbType = setItem.getDbType();

    for (DbId dbId : idSet.iter()) {
      if (!isObjReady(cache, dbType, dbId)) {
        return false;
      }
    }

    return true;
  }

  private boolean isObjReady(Cache<String, CacheItem> cache, Class<?> dbType, DbId dbId) {
    String cacheKey = dbType.getSimpleName() + '#' + dbId;
    CacheItem item = getAndCheckItem(cache, cacheKey);
    return _dbCacheUser.isAvailable(item);
  }

  private static CacheItem getAndCheckItem(Cache<String, CacheItem> cache, String key) {
    return checkNotNull(cache.getIfPresent(key), key);
  }

  private final DbCacheUser _dbCacheUser;
}
