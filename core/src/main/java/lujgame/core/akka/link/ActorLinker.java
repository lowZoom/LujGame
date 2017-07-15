package lujgame.core.akka.link;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActorContext;
import lujgame.core.akka.common.CaseActor;
import lujgame.core.akka.link.client.LinkClientActorFactory;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActorLinker {

  @Autowired
  public ActorLinker(LinkClientActorFactory linkActorFactory) {
    _linkActorFactory = linkActorFactory;
  }

  public void link(String linkUrl, CaseActor requestor, Enum<?> okMsg) {
    ActorRef reqRef = requestor.getSelf();
    Props props = _linkActorFactory.props(linkUrl, reqRef, okMsg);

    UntypedActorContext ctx = requestor.getContext();
    ctx.actorOf(props);
  }

  public void linkListen(CaseActor linstener, Enum<?> newMsg) {

  }

  private final LinkClientActorFactory _linkActorFactory;
}
