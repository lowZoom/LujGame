package lujgame.game.server.database;

import java.util.function.BiConsumer;
import org.omg.CORBA.NO_IMPLEMENT;

public class DbPreloadContext {

  public void load(Class<?> dbType, Long dbId) {
    throw new NO_IMPLEMENT("load尚未实现");
  }

  public <T> T getParam(String key, Class<T> paramType) {
    throw new NO_IMPLEMENT("getParam尚未实现");
//    return paramType.cast();
  }

  public <T> void invoke(BiConsumer<T, DbOperateContext> runner) {
    throw new NO_IMPLEMENT("invoke尚未实现");
  }
}
