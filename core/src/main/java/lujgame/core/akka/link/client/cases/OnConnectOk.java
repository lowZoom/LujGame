package lujgame.core.akka.link.client.cases;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import lujgame.core.akka.link.client.LinkClientActor;
import lujgame.core.akka.link.client.LinkClientActorState;
import lujgame.core.akka.link.message.LinkConnect;
import org.springframework.stereotype.Service;

@Service
public class OnConnectOk implements LinkClientActor.Case<LinkConnect.Ok> {

  @Override
  public void onHandle(LinkClientActor.Context ctx) {
    LinkClientActorState actorState = ctx.getActorState();
    UntypedActor actor = ctx.getActor();

    finishConnect(actorState, actor.getSender());
  }

  private void finishConnect(LinkClientActorState state, ActorRef listenRef) {
//    log().debug("link连接成功！！！！！！！！！！！！！！！！！！！");

    ActorRef reqRef = state.getRequestorRef();
    Enum<?> okMsg = state.getOkMsg();

    reqRef.tell(okMsg, listenRef);
  }
}
