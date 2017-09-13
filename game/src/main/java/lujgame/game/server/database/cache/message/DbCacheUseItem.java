package lujgame.game.server.database.cache.message;

public class DbCacheUseItem {

  public DbCacheUseItem(Class<?> dbType, String dbKey, String resultKey) {
    _dbType = dbType;
    _dbKey = dbKey;

    _resultKey = resultKey;
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

  private final Class<?> _dbType;
  private final String _dbKey;

  private final String _resultKey;
}
