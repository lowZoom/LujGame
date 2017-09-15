package lujgame.game.server.database.cache.internal;

import akka.actor.ActorRef;
import com.google.common.cache.LoadingCache;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import lujgame.game.server.database.cache.DbCacheActorState;
import lujgame.game.server.database.cache.message.DbCacheUseReq;
import org.springframework.stereotype.Component;

@Component
public class DbCacheUser {

  public CacheItem getCacheItem(LoadingCache<String, CacheItem> cache, String key) {
    try {
      return cache.get(key);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean isAvailable(CacheItem cacheItem) {
    return cacheItem.isLoadOk() && !cacheItem.isLock();
  }

  public void waitCache(DbCacheActorState state, DbCacheUseReq msg) {
    LinkedList<DbCacheUseReq> waitQueue = state.getWaitQueue();
    waitQueue.addLast(msg);
  }

  public void lockCache(CacheItem item) {
    item.setLock(true);
  }

  public void callCmd(ActorRef entityRef) {
// entityRef.tell();
  }
}
