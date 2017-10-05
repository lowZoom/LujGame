package lujgame.game.server.database.cache.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;
import lujgame.game.server.database.DbOperateContext;
import lujgame.game.server.database.DbOperateContextFactory;
import lujgame.game.server.database.cache.DbCacheActorState;
import lujgame.game.server.database.cache.message.DbCacheUseItem;
import org.springframework.beans.factory.annotation.Autowired;
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
    CacheItem item = new CacheItem(ZTestDb.class);
    cache.put(key, item);
    return item;
  }

  CacheItem safeGet(Cache<String, CacheItem> cache, String key) {
    return checkNotNull(cache.getIfPresent(key), key);
  }

  DbOperateContext makeOperateContext(ImmutableMap<String, Object> resultMap) {
    return _operateContextFactory.createContext(resultMap);
  }

  @Autowired
  private CacheKeyMaker _cacheKeyMaker;

  @Autowired
  private DbOperateContextFactory _operateContextFactory;
}
