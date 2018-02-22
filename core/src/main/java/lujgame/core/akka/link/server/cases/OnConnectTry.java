package lujgame.core.akka.link.server.cases;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import lujgame.core.akka.link.message.LinkConnect;
import lujgame.core.akka.link.server.LinkServerActor;
import lujgame.core.akka.link.server.LinkServerActorState;
import org.springframework.stereotype.Service;

@Service
public class OnConnectTry implements LinkServerActor.Case<LinkConnect.Try> {

  @Override
  public void onHandle(LinkServerActor.Context ctx) {
    LinkServerActorState actorState = ctx.getActorState();
    UntypedActor actor = ctx.getActor();

    answerConnect(actorState, actor.getSender());
  }

  public void answerConnect(LinkServerActorState state, ActorRef clientRef) {
    ActorRef listenRef = state.getListenerRef();

    // 通知本地服务端监听节点
    Enum<?> newMsg = state.getNewMsg();
    listenRef.tell(newMsg, clientRef);

    // 回复远程客户端节点
    clientRef.tell(LinkConnect.Ok.MSG, listenRef);
  }
}
