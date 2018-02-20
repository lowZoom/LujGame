package lujgame.core.akka.feature;

import java.util.Map;
import javax.inject.Inject;
import lujgame.core.spring.SpringBeanCollector;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class FeatureActorFactoryMap {

  @SuppressWarnings("unchecked")
  public FeatureActorFactory<Object, ?, ?, ?> getFactory(ActorFeature key) {
    return _factoryMap.get(key);
  }

  @EventListener(ContextRefreshedEvent.class)
  void init() {
    _factoryMap = _springBeanCollector.collectBeanMap(
        FeatureActorFactory.class, FeatureActorFactory::actorFeature);
  }

  @SuppressWarnings("rawtypes")
  private Map<ActorFeature, FeatureActorFactory> _factoryMap;

  @Inject
  private SpringBeanCollector _springBeanCollector;
}
