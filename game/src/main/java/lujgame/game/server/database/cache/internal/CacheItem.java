package lujgame.game.server.database.cache.internal;

public class CacheItem {

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

  private boolean _loadOk;
  private boolean _present;

  private boolean _lock;

  private Object _value;
}
