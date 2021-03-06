package lujgame.core.akka.feature;

import static com.google.common.base.Preconditions.checkNotNull;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.Props;
import java.util.Map;
import javax.inject.Inject;
import lujgame.core.akka.common.casev2.CaseActorContext;
import lujgame.core.akka.common.casev2.CaseActorV2;
import lujgame.core.akka.common.casev2.DefaultCaseHandler;
import lujgame.core.akka.common.casev2.ExtensionStateMap;
import org.springframework.stereotype.Service;

@Service
public class FeatureDispatchHandler implements DefaultCaseHandler<FeatureDispatchMsg> {

  @Override
  public void onHandle(CaseActorContext<?> ctx) {
    CaseActorV2<?> actor = (CaseActorV2<?>) ctx.getActor();
    ExtensionStateMap stateMap = getOrCreateStateMap(actor);
    FeatureState state = getOrCreateFeatureState(stateMap);
    Map<ActorFeature, ActorRef> featureRefMap = state.getFeatureRefMap();

    FeatureDispatchMsg msg = ctx.getMessage(this);
    ActorFeature feature = msg.getFeatureId();

    ActorRef featureRef = getOrCreateFeatureRef(featureRefMap, feature, actor.getContext());
    featureRef.tell(msg.getFeatureMsg(), actor.getSelf());
  }

  private ExtensionStateMap getOrCreateStateMap(CaseActorV2<?> actor) {
    ExtensionStateMap oldMap = actor.getExtensionStateMap();
    if (oldMap != null) {
      return oldMap;
    }
    ExtensionStateMap newMap = new ExtensionStateMap();
    actor.setExtensionStateMap(newMap);
    return newMap;
  }

  private FeatureState getOrCreateFeatureState(ExtensionStateMap stateMap) {
    FeatureState oldState = stateMap.get(ActorFeature.class);
    if (oldState != null) {
      return oldState;
    }
    FeatureState newState = new FeatureState();
    stateMap.put(ActorFeature.class, newState);
    return newState;
  }

  private ActorRef getOrCreateFeatureRef(Map<ActorFeature, ActorRef> refMap,
      ActorFeature feature, ActorContext actorContext) {
    ActorRef featureRef = refMap.get(feature);
    if (featureRef != null) {
      return featureRef;
    }

    FeatureActorFactory<Object, ?, ?, ?> factory = _featureActorFactoryMap.getFactory(feature);
    checkNotNull(factory, feature);

    Object featureState = factory.createFeatureState();
    checkNotNull(featureState, feature);

    Props props = factory.props(featureState);
    String actorName = _featureActorNameMaker.makeName(feature);
    ActorRef newRef = actorContext.actorOf(props, actorName);

    refMap.put(feature, newRef);
    return newRef;
  }

  @Inject
  private FeatureActorFactoryMap _featureActorFactoryMap;

  @Inject
  private FeatureActorNameMaker _featureActorNameMaker;
}
