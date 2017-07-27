package lujgame.game.server.database;

import lujgame.game.server.type.JStr;
import org.omg.CORBA.NO_IMPLEMENT;

public class DbOperateContext {

  public <T> T loadObject(Class<T> dbType, Long dbId) {
    throw new NO_IMPLEMENT("loadObject尚未实现");
  }

  public <T> T getParam(String key, Class<T> paramType) {
    throw new NO_IMPLEMENT("getParam尚未实现");
//    return paramType.cast();
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

  public void sendToClient(Object proto) {
    throw new NO_IMPLEMENT("sendToClient尚未实现");
  }
}
