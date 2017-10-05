package lujgame.game.server.database;

import com.google.common.collect.ImmutableMap;
import javax.annotation.Nullable;
import lujgame.game.server.database.type.DbSetTool;
import lujgame.game.server.type.JSet;
import lujgame.game.server.type.JStr;
import lujgame.game.server.type.JTime;
import org.omg.CORBA.NO_IMPLEMENT;

public class DbOperateContext {

  public DbOperateContext(
      ImmutableMap<String, Object> resultMap,
      DbSetTool dbSetTool) {
    _resultMap = resultMap;

    _dbSetTool = dbSetTool;
  }

  @Nullable
  public <T> T getDb(Class<T> dbType, String key) {
    throw new NO_IMPLEMENT("getDb尚未实现");
  }

  public <T> T getDb(JSet<T> set) {
    return _dbSetTool.getOnlyElem(set);
  }

  public <T> JSet<T> getDbSet(Class<T> dbType, String key) {
    return (JSet<T>) _resultMap.get(key);
  }

  public boolean isEmpty(JSet<?> set) {
    return _dbSetTool.isEmpty(set);
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

  private final ImmutableMap<String, Object> _resultMap;

  private final DbSetTool _dbSetTool;
}
