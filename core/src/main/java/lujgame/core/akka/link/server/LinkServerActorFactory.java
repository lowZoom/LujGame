package lujgame.core.akka.link.server;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.Creator;
import lujgame.core.akka.link.server.logic.LinkListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LinkServerActorFactory {

  @Autowired
  public LinkServerActorFactory(LinkListener linkListener) {
    _linkListener = linkListener;
  }

  public Props props(ActorRef listenRef, Enum<?> newMsg) {
    LinkServerActorState state = new LinkServerActorState(listenRef, newMsg);

    Creator<LinkServerActor> c = () ->
        new LinkServerActor(state, _linkListener);

    return Props.create(LinkServerActor.class, c);
  }

  public String getActorName() {
    return "Luj$LinkServer";
  }

  private final LinkListener _linkListener;
}
