package lujgame.core.akka.feature;

import org.springframework.stereotype.Service;

@Service
public class FeatureActorNameMaker {

  public String makeName(ActorFeature feature) {
    return "Luj$" + feature;
  }
}
