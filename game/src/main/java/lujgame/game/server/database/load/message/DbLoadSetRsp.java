package lujgame.game.server.database.load.message;

import com.google.common.collect.ImmutableSet;

public class DbLoadSetRsp {

  public DbLoadSetRsp(String cacheKey, ImmutableSet<Long> resultSet) {
    _cacheKey = cacheKey;
    _resultSet = resultSet;
  }

  public String getCacheKey() {
    return _cacheKey;
  }

  public ImmutableSet<Long> getResultSet() {
    return _resultSet;
  }

  private final String _cacheKey;

  private final ImmutableSet<Long> _resultSet;
}
