package lujgame.core.akka;

import akka.actor.ActorSystem;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorContext;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class CaseActor extends UntypedActor {

  @Override
  public void onReceive(Object msg) throws Exception {
    if (tryHandleMessage(msg)) {
      return;
    }

    logUnhandled(msg);
  }

  protected CaseActor() {
    _actionMap = new HashMap<>(32);
    _log = initLog();
  }

  protected <T extends Serializable> void addCase(Class<T> msgType, Consumer<T> action) {
    _actionMap.put(msgType, action);
  }

  protected LoggingAdapter log() {
    return _log;
  }

  private LoggingAdapter initLog() {
    UntypedActorContext ctx = getContext();
    ActorSystem system = ctx.system();

    return Logging.getLogger(system, this);
  }

  private boolean tryHandleMessage(Object msg) {
    Class<?> msgType = msg.getClass();
//      logDebug("消息类型：" + msgType);

    @SuppressWarnings("unchecked")
    Consumer<Object> action = (Consumer<Object>) _actionMap.get(msgType);

    if (action == null) {
      return false;
    }

    action.accept(msg);
    return true;
  }

  private void logUnhandled(Object msg) {
    log().warning("未处理的消息：{}（{}）", msg, msg.getClass());
    unhandled(msg);
  }

  private final Map<Class<?>, Consumer<?>> _actionMap;
  private final LoggingAdapter _log;
}
