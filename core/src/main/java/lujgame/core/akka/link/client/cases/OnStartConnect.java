package lujgame.core.akka.link.client.cases;

import akka.actor.UntypedActor;
import lujgame.core.akka.link.client.LinkClientActor;
import lujgame.core.akka.link.client.LinkClientActorState;
import lujgame.core.akka.link.client.message.StartConnectMsg;
import org.springframework.stereotype.Service;

@Service
public class OnStartConnect implements LinkClientActor.Case<StartConnectMsg> {

  @Override
  public void onHandle(LinkClientActor.Context ctx) {
    LinkClientActorState actorState = ctx.getActorState();
    UntypedActor reqActor = ctx.getActor();
    actorState.setRequestorRef(reqActor.getSelf());

    StartConnectMsg msg = ctx.getMessage(this);
    actorState.setServerUrl(msg.getLinkUrl());
    actorState.setSuccessMsg(msg.getSuccessMsg());
  }
}
