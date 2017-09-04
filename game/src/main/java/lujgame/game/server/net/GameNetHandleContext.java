package lujgame.game.server.net;

import akka.event.LoggingAdapter;
import java.util.function.BiConsumer;
import lujgame.game.server.database.DbPreloadContext;
import lujgame.game.server.type.JLong;
import lujgame.game.server.type.JStr;
import lujgame.game.server.type.Z1;
import org.omg.CORBA.NO_IMPLEMENT;

public class GameNetHandleContext {

  public GameNetHandleContext(Object proto, LoggingAdapter log, Z1 typeI) {
    _proto = proto;
    _log = log;

    _typeI = typeI;
  }

  public <T> T getPacket(GameNetHandler<T> handler) {
    return (T) _proto;
  }

  public long get(JLong val) {
    throw new NO_IMPLEMENT("get尚未实现");
  }

  public String get(JStr val) {
    return _typeI.getImpl(val).getValue();
  }

  public <T> void invoke(Class<T> actionType, BiConsumer<T, DbPreloadContext> preloader) {
    throw new NO_IMPLEMENT("invoke尚未实现");
  }

  public LoggingAdapter log() {
    return _log;
  }

  private final Object _proto;
  private final LoggingAdapter _log;

  private final Z1 _typeI;
}
