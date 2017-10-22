package lujgame.game.server.database.cache.internal;

public class CacheItem {

  public CacheItem(String cacheKey, Class<?> dbType) {
    _cacheKey = cacheKey;
    _dbType = dbType;
  }

  public String getCacheKey() {
    return _cacheKey;
  }

  public Class<?> getDbType() {
    return _dbType;
  }

  public boolean isLoadOk() {
    return _loadOk;
  }

  public void setLoadOk(boolean loadOk) {
    _loadOk = loadOk;
  }

  public boolean isPresent() {
    return _present;
  }

  public void setPresent(boolean present) {
    _present = present;
  }

  public boolean isLock() {
    return _lock;
  }

  public void setLock(boolean lock) {
    _lock = lock;
  }

  public Object getValue() {
    return _value;
  }

  public void setValue(Object value) {
    _value = value;
  }

  @Override
  public String toString() {
    return _cacheKey;
  }

  private final String _cacheKey;
  private final Class<?> _dbType;

  private boolean _loadOk;
  private boolean _present;

  private Object _value;
  private boolean _lock;
}
