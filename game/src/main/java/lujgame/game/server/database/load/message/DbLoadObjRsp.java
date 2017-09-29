package lujgame.game.server.database.load.message;

public class DbLoadObjRsp {

  public DbLoadObjRsp(String cacheKey, Class<?> dbType, Object resultObject) {
    _cacheKey = cacheKey;
    _dbType = dbType;

    _resultObject = resultObject;
  }

  public String getCacheKey() {
    return _cacheKey;
  }

  public Class<?> getDbType() {
    return _dbType;
  }

  public Object getResultObject() {
    return _resultObject;
  }

  private final String _cacheKey;
  private final Class<?> _dbType;

  private final Object _resultObject;
}
