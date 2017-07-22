package lujgame.core.akka.link.server.logic;

import akka.actor.ActorRef;
import lujgame.core.akka.link.message.LinkConnect;
import lujgame.core.akka.link.server.LinkServerActorState;
import org.springframework.stereotype.Component;

@Component
public class LinkListener {

  public void answerConnect(LinkServerActorState state, ActorRef clientRef) {
    ActorRef listenRef = state.getListenerRef();

    // 通知本地服务端监听节点
    Enum<?> newMsg = state.getNewMsg();
    listenRef.tell(newMsg, clientRef);

    // 回复远程客户端节点
    clientRef.tell(LinkConnect.Ok.MSG, listenRef);
  }
}
