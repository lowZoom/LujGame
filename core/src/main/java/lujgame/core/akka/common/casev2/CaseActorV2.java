package lujgame.core.akka.common.casev2;

import akka.actor.ActorSystem;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.Map;
import java.util.function.Supplier;

public abstract class CaseActorV2<S> extends UntypedActor {

  @Override
  public void preStart() throws Exception {
    if (_preStartHandler == null) {
      return;
    }
    CaseActorContext<S> ctx = createContext(null);
    _preStartHandler.preStart(ctx);
  }

  @Override
  public void postStop() throws Exception {
    if (_postStopHandler == null) {
      return;
    }
    CaseActorContext<S> ctx = createContext(null);
    _postStopHandler.postStop(ctx);
  }

  @Override
  public void onReceive(Object msg) throws Throwable {
    Class<?> msgType = msg.getClass();
    ActorCaseHandler<Object, ?> msgHandler = (ActorCaseHandler<Object, ?>) _caseHandlerMap
        .get(msgType);

    CaseActorContext<S> ctx = createContext(msg);
    msgHandler.onHandle(ctx);
  }

  public void setState(S state) {
    _state = state;
  }

  public void setContextConstructor(Supplier<CaseActorContext<S>> contextConstructor) {
    _contextConstructor = contextConstructor;
  }

  public void setPreStartHandler(PreStartHandler<CaseActorContext<S>> preStartHandler) {
    _preStartHandler = preStartHandler;
  }

  public void setPostStopHandler(PostStopHandler<CaseActorContext<S>> postStopHandler) {
    _postStopHandler = postStopHandler;
  }

  public void setCaseHandlerMap(Map<Class<?>, ?> caseHandlerMap) {
    _caseHandlerMap = caseHandlerMap;
  }

  protected CaseActorV2() {
    ActorSystem system = getContext().system();
    _logger = Logging.getLogger(system, this);
  }

  private CaseActorContext<S> createContext(Object msg) {
    CaseActorContext<S> ctx = _contextConstructor.get();
    ctx._actorState = _state;
    ctx._message = msg;
    ctx._actorLogger = _logger;
    ctx._actor = this;
    return ctx;
  }

  private S _state;
  private Supplier<CaseActorContext<S>> _contextConstructor;

  private PreStartHandler<CaseActorContext<S>> _preStartHandler;
  private PostStopHandler<CaseActorContext<S>> _postStopHandler;

  private Map<Class<?>, ?> _caseHandlerMap;

  private final LoggingAdapter _logger;
}
