package lujgame.game.server.database;

import akka.actor.ActorRef;
import com.google.common.collect.ImmutableMap;
import javax.annotation.Nullable;
import lujgame.game.server.database.bean.DatabaseMeta;
import lujgame.game.server.database.type.DbObjTool;
import lujgame.game.server.database.type.DbSetTool;
import lujgame.game.server.type.JSet;
import lujgame.game.server.type.JStr;
import lujgame.game.server.type.JTime;
import org.omg.CORBA.NO_IMPLEMENT;

public class DbOperateContext {

  public DbOperateContext(
      ImmutableMap<String, Object> resultMap,
      ImmutableMap<Class<?>, DatabaseMeta> databaseMetaMap,
      ActorRef connRef,
      DbSetTool dbSetTool,
      DbObjTool dbObjTool) {
    _resultMap = resultMap;

    _databaseMetaMap = databaseMetaMap;
    _connRef = connRef;

    _dbSetTool = dbSetTool;
    _dbObjTool = dbObjTool;
  }

  public <T> T getPacket(Class<T> packetType) {
    throw new NO_IMPLEMENT("getPacket尚未实现");
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

  public <T> T createDb(Class<T> dbType, JSet<T> dbSet) {
    return _dbObjTool.createObj(dbType, _databaseMetaMap, now());
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
    //TODO: 走单独的包，可能直接在业务层实现会比较好
    throw new NO_IMPLEMENT("sendError2C尚未实现");
  }

  public void sendResponse2C(Object proto) {
    throw new NO_IMPLEMENT("sendToClient尚未实现");
  }

  public JTime now() {
    throw new NO_IMPLEMENT("now尚未实现");
  }

  private final ImmutableMap<String, Object> _resultMap;

  private final ImmutableMap<Class<?>, DatabaseMeta> _databaseMetaMap;
  private final ActorRef _connRef;

  private final DbSetTool _dbSetTool;
  private final DbObjTool _dbObjTool;
}
