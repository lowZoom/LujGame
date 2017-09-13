package lujgame.game.server.database.cache.internal;

import akka.actor.ActorRef;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Multimap;
import java.util.concurrent.ExecutionException;
import lujgame.game.server.database.cache.DbCacheActorState;
import lujgame.game.server.database.cache.message.DbCacheUseReq;
import org.springframework.stereotype.Component;

@Component
public class DbCacheUser {

  public String makeCacheKey(Class<?> dbType) {
    return dbType.getSimpleName();
  }

  public CacheItem getCacheItem(LoadingCache<String, CacheItem> cache, String key) {
    try {
      return cache.get(key);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean isAvailable(CacheItem cacheItem) {
    return cacheItem._loadOk && !cacheItem._lock;
  }

  public void waitCache(DbCacheActorState state, DbCacheUseReq msg) {
    Multimap<String, DbCacheUseReq> waitMap = state.getWaitingMap();

  }

  public void lockCache(CacheItem item) {
    item._lock = true;
  }

  public void callCmd(ActorRef entityRef) {
// entityRef.tell();
  }
}
