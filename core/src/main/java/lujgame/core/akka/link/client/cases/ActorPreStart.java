package lujgame.core.akka.link.client.cases;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import lujgame.core.akka.link.client.LinkClientActor;
import org.springframework.stereotype.Service;

@Service
public class ActorPreStart implements LinkClientActor.PreStart {

  /**
   * @see OnTryConnect
   */
  @Override
  public void preStart(LinkClientActor.Context ctx) throws Exception {
    UntypedActor actor = ctx.getActor();
    ActorRef actorRef = actor.getSelf();

    actorRef.tell(LinkClientActor.TryConnect.MSG, actorRef);
  }
}
