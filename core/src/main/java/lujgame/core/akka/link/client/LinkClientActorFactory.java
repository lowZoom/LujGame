package lujgame.core.akka.link.client;

import akka.actor.Props;
import akka.japi.Creator;
import lujgame.core.akka.link.client.logic.LinkConnector;
import lujgame.core.akka.schedule.ActorScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LinkClientActorFactory {

  @Autowired
  public LinkClientActorFactory(
      LinkConnector linkConnector) {
    _linkConnector = linkConnector;
  }

  public Props props(String linkUrl) {
    LinkClientActorState state = new LinkClientActorState(linkUrl);

    Creator<LinkClientActor> c = () ->
        new LinkClientActor(state, _linkConnector);

    return Props.create(LinkClientActor.class, c);
  }

  private final LinkConnector _linkConnector;
}
