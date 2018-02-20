package lujgame.core.akka.feature;

import akka.actor.ActorRef;
import java.util.EnumMap;
import java.util.Map;

public class FeatureState {

  public FeatureState() {
    _featureRefMap = new EnumMap<>(ActorFeature.class);
  }

  public Map<ActorFeature, ActorRef> getFeatureRefMap() {
    return _featureRefMap;
  }

  private final Map<ActorFeature, ActorRef> _featureRefMap;
}
