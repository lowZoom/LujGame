package lujgame.game.server.database.cache.message;

public class DbCacheUseItem {

  public DbCacheUseItem(String cacheKey, Class<?> dbType, String dbKey, String resultKey) {
    _cacheKey = cacheKey;

    _dbType = dbType;
    _dbKey = dbKey;

    _resultKey = resultKey;
  }

  public String getCacheKey() {
    return _cacheKey;
  }

  public Class<?> getDbType() {
    return _dbType;
  }

  public String getDbKey() {
    return _dbKey;
  }

  public String getResultKey() {
    return _resultKey;
  }

  private final String _cacheKey;

  private final Class<?> _dbType;
  private final String _dbKey;

  private final String _resultKey;
}
