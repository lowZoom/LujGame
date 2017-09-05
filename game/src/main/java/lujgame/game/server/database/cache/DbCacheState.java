package lujgame.game.server.database.cache;

import com.google.common.cache.LoadingCache;

public class DbCacheState {

  public DbCacheState(LoadingCache<String, Object> cache) {
    _cache = cache;
  }


  private final LoadingCache<String, Object> _cache;
}
