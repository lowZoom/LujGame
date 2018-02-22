package lujgame.core.akka.link.client;

import lujgame.core.akka.feature.ActorFeature;
import lujgame.core.akka.feature.FeatureActorFactory;
import org.springframework.stereotype.Service;

@Service
public class LinkClientActorFactory extends FeatureActorFactory<
    LinkClientActorState,
    LinkClientActor,
    LinkClientActor.Context,
    LinkClientActor.Case<?>> {

  @Override
  protected LinkClientActor createActor() {
    return new LinkClientActor();
  }

  @Override
  protected LinkClientActor.Context createContext() {
    return new LinkClientActor.Context();
  }

  @Override
  protected Class<LinkClientActor.PreStart> preStart() {
    return LinkClientActor.PreStart.class;
  }

  @Override
  public ActorFeature actorFeature() {
    return ActorFeature.LINK_CLIENT;
  }

  @Override
  public LinkClientActorState createFeatureState() {
    return new LinkClientActorState();
  }
}
