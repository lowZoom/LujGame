package lujgame.game.server.net;

import java.util.function.BiConsumer;
import lujgame.game.server.database.DbPreloadContext;
import lujgame.game.server.type.JLong;
import lujgame.game.server.type.JStr;
import org.omg.CORBA.NO_IMPLEMENT;

public class GameNetHandleContext {

  public GameNetHandleContext(Object proto) {
    _proto = proto;
  }

  public <T> T getPacket(GameNetHandler<T> handler) {
    return (T) _proto;
  }

  public long get(JLong val) {
    throw new NO_IMPLEMENT("get尚未实现");
  }

  public String get(JStr val) {
    throw new NO_IMPLEMENT("get尚未实现");
  }

  public <T> void invoke(Class<T> actionType, BiConsumer<T, DbPreloadContext> preloader) {

  }

  private final Object _proto;
}
