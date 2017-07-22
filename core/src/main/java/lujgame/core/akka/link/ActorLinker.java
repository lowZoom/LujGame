package lujgame.core.akka.link;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActorContext;
import lujgame.core.akka.common.CaseActor;
import lujgame.core.akka.link.client.LinkClientActorFactory;
import lujgame.core.akka.link.server.LinkServerActorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActorLinker {

  @Autowired
  public ActorLinker(
      LinkClientActorFactory clientActorFactory,
      LinkServerActorFactory serverActorFactory) {
    _clientActorFactory = clientActorFactory;
    _serverActorFactory = serverActorFactory;
  }

  public void link(String linkUrl, CaseActor requestor, Enum<?> okMsg) {
    ActorRef reqRef = requestor.getSelf();
    String serverUrl = linkUrl + '/' + _serverActorFactory.getActorName();
    Props props = _clientActorFactory.props(serverUrl, reqRef, okMsg);

    UntypedActorContext ctx = requestor.getContext();
    ctx.actorOf(props);
  }

  public void linkListen(CaseActor listener, Enum<?> newMsg) {
    LinkServerActorFactory f = _serverActorFactory;
    ActorRef listenRef = listener.getSelf();
    Props props = f.props(listenRef, newMsg);

    UntypedActorContext ctx = listener.getContext();
    ctx.actorOf(props, f.getActorName());
  }

  private final LinkClientActorFactory _clientActorFactory;
  private final LinkServerActorFactory _serverActorFactory;
}
