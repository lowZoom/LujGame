package lujgame.core.akka.common.casev2;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import javax.inject.Inject;
import lujgame.core.spring.SpringBeanCollector;
import org.omg.CORBA.NO_IMPLEMENT;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class CaseHandlerCollector {

  public <C extends ActorCaseHandler> Map<Class<?>, C> collect(Class<C> handlerType) {
    throw new NO_IMPLEMENT("collect尚未实现");
  }

  @EventListener(ContextRefreshedEvent.class)
  void init() {
    _defaultHandlerMap = collectImpl(DefaultCaseHandler.class);
  }

  private <C extends ActorCaseHandler>
  Map<Class<?>, ActorCaseHandler> collectImpl(Class<C> handlerType) {
    Map<Class<?>, C> resultMap = _springBeanCollector
        .collectBeanMap(handlerType, this::getMessageType);
    return (Map<Class<?>, ActorCaseHandler>) resultMap;
  }

  private <C> Class<?> getMessageType(C handler) {
    Type parentType = handler.getClass().getGenericInterfaces()[0];
    return (Class<?>) getTypeArgument(parentType, 0);
  }

  private Type getTypeArgument(Type type, int argIndex) {
    return ((ParameterizedType) type).getActualTypeArguments()[argIndex];
  }

  private Map<Class<?>, ActorCaseHandler> _defaultHandlerMap;

  @Inject
  private SpringBeanCollector _springBeanCollector;
}
