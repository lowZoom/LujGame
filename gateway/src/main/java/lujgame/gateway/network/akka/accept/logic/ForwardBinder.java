package lujgame.gateway.network.akka.accept.logic;

import akka.actor.ActorRef;
import java.util.Map;
import lujgame.gateway.network.akka.accept.NetAcceptState;
import lujgame.gateway.network.akka.accept.message.BindForwardReq;
import lujgame.gateway.network.akka.accept.message.BindForwardRsp;
import org.springframework.stereotype.Component;

@Component
public class ForwardBinder {

//  public void bind(NetAcceptState state, BindForwardReq req,
//      ActorRef connRef, ActorRef acceptRef) {
//    Map<String, ActorRef> forwardMap = state.getForwardMap();
//    String boxId = req.getBoxId();
//    ActorRef forwardRef = forwardMap.get(boxId);
//
//    if (forwardRef != null) {
//      BindForwardRsp rsp = new BindForwardRsp(forwardRef);
//      connRef.tell(rsp, acceptRef);
//      return;
//    }
//
//    // 发送到网关中心服查询
//    ActorRef gateMasterRef = state.getGateMasterRef();
//    gateMasterRef.tell(req, acceptRef);
//  }
}
