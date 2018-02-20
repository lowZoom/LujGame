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
    CaseActorContext<S> ctx = createContext(NO_MSG);
    _preStartHandler.preStart(ctx);
  }

  @Override
  public void postStop() throws Exception {
    if (_postStopHandler == null) {
      return;
    }
    CaseActorContext<S> ctx = createContext(NO_MSG);
    _postStopHandler.postStop(ctx);
  }

  @Override
  public void onReceive(Object msg) throws Throwable {
    tryHandleCase(msg);
  }

  public ExtensionStateMap getExtensionStateMap() {
    return _extensionStateMap;
  }

  public void setExtensionStateMap(ExtensionStateMap extensionStateMap) {
    _extensionStateMap = extensionStateMap;
  }

  public void setActorState(S actorState) {
    _actorState = actorState;
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

  @SuppressWarnings("unchecked")
  private void tryHandleCase(Object msg) {
    Class<?> msgType = msg.getClass();
    ActorCaseHandler<Object, ?> msgHandler =
        (ActorCaseHandler<Object, ?>) _caseHandlerMap.get(msgType);

    if (msgHandler == null) {
      logUnhandled(msg);
      return;
    }

    CaseActorContext<S> ctx = createContext(msg);
    msgHandler.onHandle(ctx);
  }

  private void logUnhandled(Object msg) {
    _logger.warning("未处理的消息：{}（{}）", msg, msg.getClass());
    unhandled(msg);
  }

  private CaseActorContext<S> createContext(Object msg) {
    CaseActorContext<S> ctx = _contextConstructor.get();
    ctx._actorState = _actorState;
    ctx._message = msg;
    ctx._actorLogger = _logger;
    ctx._actor = this;
    return ctx;
  }

  private static final Object NO_MSG = null;

  private ExtensionStateMap _extensionStateMap;

  private S _actorState;
  private Supplier<CaseActorContext<S>> _contextConstructor;

  private PreStartHandler<CaseActorContext<S>> _preStartHandler;
  private PostStopHandler<CaseActorContext<S>> _postStopHandler;
  private Map<Class<?>, ?> _caseHandlerMap;

  private final LoggingAdapter _logger;
}
