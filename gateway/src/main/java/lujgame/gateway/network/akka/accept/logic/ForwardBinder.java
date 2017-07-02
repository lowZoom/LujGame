package lujgame.gateway.network.akka.accept.logic;

import akka.actor.ActorRef;
import java.util.Map;
import lujgame.gateway.network.akka.accept.NetAcceptState;
import lujgame.gateway.network.akka.accept.message.BindForwardReq;
import lujgame.gateway.network.akka.accept.message.BindForwardRsp;

public class ForwardBinder {

  public void tryBindLocally(NetAcceptState state, BindForwardReq req,
      ActorRef connRef, ActorRef acceptRef) {
    Map<String, ActorRef> forwardMap = state.getForwardMap();
    String boxId = req.getBoxId();
    ActorRef forwardRef = forwardMap.get(boxId);

    if (forwardRef != null) {
      //TODO: 直接回复

      BindForwardRsp rsp = new BindForwardRsp(forwardRef);
      connRef.tell(rsp, acceptRef);

      return;
    }

    //TODO: 发送到网关中心服查询
    ActorRef gateBossRef = state.getGateBossRef();
    gateBossRef.tell(req, acceptRef);
  }
}
