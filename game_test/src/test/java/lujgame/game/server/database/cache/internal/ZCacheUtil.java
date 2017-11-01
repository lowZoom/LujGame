package lujgame.game.server.database.cache.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.mockito.Mockito.mock;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import javax.inject.Inject;
import lujgame.game.server.database.cache.DbCacheActorState;
import lujgame.game.server.database.cache.message.DbCacheUseItem;
import lujgame.game.server.database.operate.DbOperateContext;
import lujgame.game.server.database.operate.DbOperateContextFactory;
import org.springframework.stereotype.Component;

@Component
class ZCacheUtil {

  DbCacheActorState createCacheState() {
    DbCacheActorState state = new DbCacheActorState(null);

    Cache<String, CacheItem> cache = CacheBuilder.newBuilder().build();
    state.setCache(cache);

    return state;
  }

  String makeSetKey(String dbKey) {
    return _cacheKeyMaker.makeSetKey(ZTestDb.class, dbKey);
  }

  String makeObjectKey(long dbId) {
    return _cacheKeyMaker.makeObjectKey(ZTestDb.class, dbId);
  }

  DbCacheUseItem makeUseItem(String cacheKey, String resultKey) {
    return new DbCacheUseItem(cacheKey, ZTestDb.class, cacheKey.split("#")[1], resultKey);
  }

  CacheItem addCacheItem(Cache<String, CacheItem> cache, String key) {
    CacheItem item = new CacheItem(key, ZTestDb.class);
    cache.put(key, item);
    return item;
  }

  CacheItem getNotNull(Cache<String, CacheItem> cache, String key) {
    return checkNotNull(cache.getIfPresent(key), key);
  }

  DbOperateContext makeOperateContext(ImmutableMap<String, Object> resultMap, ActorRef connRef) {
    return _operateContextFactory.createContext(0, ImmutableMap.of(), resultMap, ImmutableSet.of(),
        ImmutableMap.of(), ImmutableMap.of(), connRef, null, mock(LoggingAdapter.class));
  }

  @Inject
  private CacheKeyMaker _cacheKeyMaker;

  @Inject
  private DbOperateContextFactory _operateContextFactory;
}
