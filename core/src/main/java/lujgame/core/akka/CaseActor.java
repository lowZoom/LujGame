package lujgame.core.akka;


import akka.actor.ActorPath;
import akka.actor.ActorRef;
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
    _actionMap = new HashMap<>();

    initLog();
  }

  protected <T extends Serializable> void addCase(Class<T> msgType, Consumer<T> action) {
    _actionMap.put(msgType, action);
  }

  protected LoggingAdapter log() {
    return _log;
  }

  private void initLog() {
    UntypedActorContext ctx = getContext();
    ActorSystem system = ctx.system();

    _log = Logging.getLogger(system, this);
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
    ActorRef self = getSelf();
    ActorPath path = self.path();

    log().warning("{} 未处理的消息：{}（{}）", path, msg, msg.getClass());
    unhandled(msg);
  }

  private Map<Class<?>, Consumer<?>> _actionMap;
  private LoggingAdapter _log;
}
