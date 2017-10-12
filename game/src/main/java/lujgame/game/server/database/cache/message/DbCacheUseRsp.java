package lujgame.game.server.database.cache.message;

import com.google.common.collect.ImmutableMap;

public class DbCacheUseRsp {

  public DbCacheUseRsp(
      Class<?> cmdType,
      ImmutableMap<String, Object> paramMap,
      ImmutableMap<String, Object> resultMap,
      int requestTime) {
    _cmdType = cmdType;

    _paramMap = paramMap;
    _resultMap = resultMap;

    _requestTime = requestTime;
  }

  public Class<?> getCmdType() {
    return _cmdType;
  }

  public ImmutableMap<String, Object> getParamMap() {
    return _paramMap;
  }

  public ImmutableMap<String, Object> getResultMap() {
    return _resultMap;
  }

  public int getRequestTime() {
    return _requestTime;
  }

  private final Class<?> _cmdType;

  private final ImmutableMap<String, Object> _paramMap;
  private final ImmutableMap<String, Object> _resultMap;

  private final int _requestTime;
}
