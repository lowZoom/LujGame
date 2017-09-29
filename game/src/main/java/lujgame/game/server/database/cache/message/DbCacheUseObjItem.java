package lujgame.game.server.database.cache.message;

public class DbCacheUseObjItem {

  public DbCacheUseObjItem(String cacheKey, Class<?> dbType, Long dbId, String resultKey) {
    _cacheKey = cacheKey;

    _dbType = dbType;
    _dbId = dbId;

    _resultKey = resultKey;
  }

  public String getCacheKey() {
    return _cacheKey;
  }

  public Class<?> getDbType() {
    return _dbType;
  }

  public String getResultKey() {
    return _resultKey;
  }

  private final String _cacheKey;

  private final Class<?> _dbType;
  private final Long _dbId;

  private final String _resultKey;
}
