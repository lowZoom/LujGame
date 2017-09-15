package lujgame.game.server.database.cache.message;

import com.google.common.collect.ImmutableMap;
import lujgame.game.server.database.cache.internal.CacheItem;

public class DbCacheUseRsp {

  public DbCacheUseRsp(
      ImmutableMap<String, CacheItem> resultMap,
      Class<?> cmdType,
      int requestTime) {
    _cmdType = cmdType;
    _resultMap = resultMap;

    _requestTime = requestTime;
  }

  public Class<?> getCmdType() {
    return _cmdType;
  }

  public ImmutableMap<String, CacheItem> getResultMap() {
    return _resultMap;
  }

  private final Class<?> _cmdType;
  private final ImmutableMap<String, CacheItem> _resultMap;

  private final int _requestTime;
}
