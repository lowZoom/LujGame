package lujgame.core.akka.common.casev2;

import akka.actor.Props;
import akka.japi.Creator;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Supplier;
import javax.inject.Inject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;

public abstract class CaseActorFactory<S, A extends CaseActorV2<S>,
    CO extends CaseActorContext<S>, C extends ActorCaseHandler<CO, ?>> {

  public Props props(S state) {
    Creator<A> c = () -> {
      A actor = createActor();
      actor.setActorState(state);
      actor.setContextConstructor(_contextConstructor);
      actor.setPreStartHandler(_preStartHandler);
      actor.setPostStopHandler(_postStopHandler);
      actor.setCaseHandlerMap(_caseHandlerMap);
      return actor;
    };
    return Props.create(_actorType, c);
  }

  protected abstract A createActor();

  protected abstract CO createContext();

  protected Class<? extends PreStartHandler<CO>> preStart() {
    return null;
  }

  protected Class<? extends PostStopHandler<CO>> postStop() {
    return null;
  }

  @SuppressWarnings("unchecked")
  @EventListener(CaseHandlerCollector.AfterInit.class)
  void init() {
    Type factoryType = getClass().getGenericSuperclass();
    _actorType = (Class<A>) getTypeArgument(factoryType, 1);
    _contextConstructor = this::createContext;

    _preStartHandler = getPreStartHandler();
    _postStopHandler = getPostStopHandler();

    _caseHandlerMap = collectCaseHandlerMap(factoryType);
  }

  @SuppressWarnings("unchecked")
  private PreStartHandler<CaseActorContext<S>> getPreStartHandler() {
    Class<?> preStartType = preStart();
    if (preStartType == null) {
      return null;
    }
    return (PreStartHandler<CaseActorContext<S>>) _applicationContext.getBean(preStartType);
  }

  @SuppressWarnings("unchecked")
  private PostStopHandler<CaseActorContext<S>> getPostStopHandler() {
    Class<?> postStopType = postStop();
    if (postStopType == null) {
      return null;
    }
    return (PostStopHandler<CaseActorContext<S>>) _applicationContext.getBean(postStopType);
  }

  @SuppressWarnings("unchecked")
  private Map<Class<?>, C> collectCaseHandlerMap(Type factoryType) {
    ParameterizedType caseType = (ParameterizedType) getTypeArgument(factoryType, 3);
    return _caseHandlerCollector.collect((Class<C>) caseType.getRawType());
  }

  private Type getTypeArgument(Type type, int argIndex) {
    return ((ParameterizedType) type).getActualTypeArguments()[argIndex];
  }

  private Class<A> _actorType;
  private Supplier<CaseActorContext<S>> _contextConstructor;

  private PreStartHandler<CaseActorContext<S>> _preStartHandler;
  private PostStopHandler<CaseActorContext<S>> _postStopHandler;

  private Map<Class<?>, C> _caseHandlerMap;

  @Inject
  private ApplicationContext _applicationContext;

  @Inject
  private CaseHandlerCollector _caseHandlerCollector;
}
