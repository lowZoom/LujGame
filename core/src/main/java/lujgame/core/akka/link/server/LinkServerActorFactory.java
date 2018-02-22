package lujgame.core.akka.link.server;

import lujgame.core.akka.feature.ActorFeature;
import lujgame.core.akka.feature.FeatureActorFactory;
import org.springframework.stereotype.Service;

@Service
public class LinkServerActorFactory extends FeatureActorFactory<
    LinkServerActorState,
    LinkServerActor,
    LinkServerActor.Context,
    LinkServerActor.Case<?>> {

  @Override
  protected LinkServerActor createActor() {
    return new LinkServerActor();
  }

  @Override
  protected LinkServerActor.Context createContext() {
    return new LinkServerActor.Context();
  }

  @Override
  public ActorFeature actorFeature() {
    return ActorFeature.LINK_SERVER;
  }

  @Override
  public LinkServerActorState createFeatureState() {
    return new LinkServerActorState();
  }
}
