package lujgame.core.akka.common.casev2;

import akka.actor.ActorSystem;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.Map;

public abstract class CaseActorV2<S> extends UntypedActor {

  @Override
  public void onReceive(Object msg) throws Throwable {
    Class<?> msgType = msg.getClass();
    ActorCaseHandler<Object, ?> msgHandler = (ActorCaseHandler<Object, ?>) getHandlerMap()
        .get(msgType);

    CaseContext<S> ctx = createContext();
    ctx._actorState = getState();
    ctx._message = msg;
    ctx._actorLogger = _logger;
    ctx._actor = this;

    msgHandler.onHandle(ctx);
  }

  protected CaseActorV2() {
    ActorSystem system = getContext().system();
    _logger = Logging.getLogger(system, this);
  }

  protected abstract CaseContext<S> createContext();

  protected abstract S getState();

  protected abstract Map<Class<?>, ?> getHandlerMap();

  private final LoggingAdapter _logger;
}
