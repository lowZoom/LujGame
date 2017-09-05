package lujgame.game.server.database;

import javax.annotation.Nullable;
import lujgame.game.server.type.JStr;
import lujgame.game.server.type.JTime;
import org.omg.CORBA.NO_IMPLEMENT;

public class DbOperateContext {

  public <T> T loadObject(Class<T> dbType, Long dbId) {
    throw new NO_IMPLEMENT("loadObject尚未实现");
  }

  @Nullable
  public <T> T getDb(Class<T> dbType, String key) {
    throw new NO_IMPLEMENT("loadObject尚未实现");
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
}
