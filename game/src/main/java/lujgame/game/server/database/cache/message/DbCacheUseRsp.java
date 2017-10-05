package lujgame.game.server.database.cache.message;

import com.google.common.collect.ImmutableMap;

public class DbCacheUseRsp {

  public DbCacheUseRsp(
      ImmutableMap<String, Object> resultMap,
      Class<?> cmdType,
      int requestTime) {
    _cmdType = cmdType;
    _resultMap = resultMap;

    _requestTime = requestTime;
  }

  public Class<?> getCmdType() {
    return _cmdType;
  }

  public ImmutableMap<String, Object> getResultMap() {
    return _resultMap;
  }

  public int getRequestTime() {
    return _requestTime;
  }

  private final Class<?> _cmdType;
  private final ImmutableMap<String, Object> _resultMap;

  private final int _requestTime;
}
