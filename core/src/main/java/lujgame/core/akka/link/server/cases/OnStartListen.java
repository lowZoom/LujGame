package lujgame.core.akka.link.server.cases;

import akka.actor.UntypedActor;
import lujgame.core.akka.link.server.LinkServerActor;
import lujgame.core.akka.link.server.LinkServerActorState;
import lujgame.core.akka.link.server.message.StartListenMsg;
import org.springframework.stereotype.Service;

@Service
public class OnStartListen implements LinkServerActor.Case<StartListenMsg> {

  @Override
  public void onHandle(LinkServerActor.Context ctx) {
    LinkServerActorState actorState = ctx.getActorState();
    UntypedActor actor = ctx.getActor();
    actorState.setListenerRef(actor.getSender());

    StartListenMsg msg = ctx.getMessage(this);
    actorState.setNewMsg(msg.getNewLinkMsg());
  }
}
