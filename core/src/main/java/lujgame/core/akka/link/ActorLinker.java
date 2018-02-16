package lujgame.core.akka.link;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorContext;
import javax.inject.Inject;
import lujgame.core.akka.common.CaseActor;
import lujgame.core.akka.link.client.LinkClientActor;
import lujgame.core.akka.link.client.LinkClientActorFactory;
import lujgame.core.akka.link.client.LinkClientActorState;
import lujgame.core.akka.link.server.LinkServerActorFactory;
import org.springframework.stereotype.Service;

/**
 * 负责链接两个Actor，可以日后持续通讯
 */
@Service
public class ActorLinker {

  /**
   * @see LinkClientActor
   */
  public void link(String linkUrl, UntypedActor requestor, Enum<?> okMsg) {
    ActorRef reqRef = requestor.getSelf();
    String serverUrl = linkUrl + '/' + _serverActorFactory.getActorName();

    LinkClientActorState state = new LinkClientActorState(serverUrl, reqRef, okMsg);
    Props props = _clientActorFactory.props(state);

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

  @Inject
  private LinkClientActorFactory _clientActorFactory;

  @Inject
  private LinkServerActorFactory _serverActorFactory;
}
