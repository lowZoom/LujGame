package lujgame.core.akka.common.casev2;

import akka.actor.Props;
import akka.japi.Creator;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Supplier;
import javax.inject.Inject;
import lujgame.core.spring.SpringBeanCollector;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

public abstract class CaseActorFactory<S, A extends CaseActorV2<S>,
    CO extends CaseActorContext<S>, C extends ActorCaseHandler<CO, ?>> {

  public Props props(S state) {
    Creator<A> c = () -> {
      A actor = actorConstructor().get();
      actor.setState(state);
      actor.setContextConstructor(_contextConstructor);
      actor.setPreStartHandler(_preStartHandler);
      actor.setPostStopHandler(_postStopHandler);
      actor.setCaseHandlerMap(_caseHandlerMap);
      return actor;
    };
    return Props.create(actorType(), c);
  }

  protected abstract Class<A> actorType();

  protected abstract Supplier<A> actorConstructor();

  protected abstract Supplier<CO> contextConstructor();

  protected Class<? extends PreStartHandler<CO>> preStart() {
    return null;
  }

  protected Class<? extends PostStopHandler<CO>> postStop() {
    return null;
  }

  @SuppressWarnings("unchecked")
  @EventListener(ContextRefreshedEvent.class)
  void init() {
    _contextConstructor = (Supplier<CaseActorContext<S>>) contextConstructor();

    _preStartHandler = getPreStartHandler();
    _postStopHandler = getPostStopHandler();

    _caseHandlerMap = collectCaseHandlerMap();
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
  private Map<Class<?>, C> collectCaseHandlerMap() {
    Type factoryType = getClass().getGenericSuperclass();
    ParameterizedType caseType = (ParameterizedType) getTypeArgument(factoryType, 3);
    return _beanCollector.collectBeanMap((Class<C>) caseType.getRawType(), this::getMessageType);
  }

  private Class<?> getMessageType(C handler) {
    Type parentType = handler.getClass().getGenericInterfaces()[0];
    return (Class<?>) getTypeArgument(parentType, 0);
  }

  private Type getTypeArgument(Type type, int argIndex) {
    return ((ParameterizedType) type).getActualTypeArguments()[argIndex];
  }

  private Supplier<CaseActorContext<S>> _contextConstructor;

  private PreStartHandler<CaseActorContext<S>> _preStartHandler;
  private PostStopHandler<CaseActorContext<S>> _postStopHandler;

  private Map<Class<?>, C> _caseHandlerMap;

  @Inject
  private ApplicationContext _applicationContext;

  @Inject
  private SpringBeanCollector _beanCollector;
}
