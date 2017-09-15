package lujgame.game.server.database;

import com.google.common.collect.ImmutableMap;
import javax.annotation.Nullable;
import lujgame.game.server.database.cache.internal.CacheItem;
import lujgame.game.server.type.JSet;
import lujgame.game.server.type.JStr;
import lujgame.game.server.type.JTime;
import org.omg.CORBA.NO_IMPLEMENT;

public class DbOperateContext {

  public DbOperateContext(
      ImmutableMap<String, CacheItem> resultMap) {
    _resultMap = resultMap;
  }

  @Nullable
  public <T> T getDb(Class<T> dbType, String key) {
    CacheItem cacheItem = _resultMap.get(key);
    return (T) cacheItem.getValue();
  }

  public <T> T getDb(JSet<T> set) {
    throw new NO_IMPLEMENT("getDb尚未实现");
  }

  public <T> JSet<T> getDbSet(Class<T> dbType, String key) {
    CacheItem cacheItem = _resultMap.get(key);
    return (JSet<T>) cacheItem.getValue();
  }

  public boolean isEmpty(JSet<?> set) {
    throw new NO_IMPLEMENT("isEmpty尚未实现");
  }

  public <T> T createProto(Class<T> protoType) {
    throw new NO_IMPLEMENT("createProto尚未实现");
  }

  public void jSet(JStr field, String value) {
    throw new NO_IMPLEMENT("dbSet尚未实现");
  }

  public void jSet(JStr from, JStr to) {
    throw new NO_IMPLEMENT("copy尚未实现");
  }

  public void jSet(JTime field, JTime value) {
    throw new NO_IMPLEMENT("copy尚未实现");
  }

  public void sendError2C() {
    throw new NO_IMPLEMENT("sendError2C尚未实现");
  }

  public void sendResponse2C(Object proto) {
    throw new NO_IMPLEMENT("sendToClient尚未实现");
  }

  public JTime now() {
    throw new NO_IMPLEMENT("now尚未实现");
  }

  private final ImmutableMap<String, CacheItem> _resultMap;
}
