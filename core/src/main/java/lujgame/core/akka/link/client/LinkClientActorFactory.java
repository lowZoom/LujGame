package lujgame.core.akka.link.client;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.Creator;
import lujgame.core.akka.link.client.logic.LinkConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LinkClientActorFactory {

  @Autowired
  public LinkClientActorFactory(
      LinkConnector linkConnector) {
    _linkConnector = linkConnector;
  }

  public Props props(String linkUrl, ActorRef requestorRef, Enum<?> okMsg) {
    LinkClientActorState state = new LinkClientActorState(linkUrl, requestorRef, okMsg);

    Creator<LinkClientActor> c = () ->
        new LinkClientActor(state, _linkConnector);

    return Props.create(LinkClientActor.class, c);
  }

  private final LinkConnector _linkConnector;
}
