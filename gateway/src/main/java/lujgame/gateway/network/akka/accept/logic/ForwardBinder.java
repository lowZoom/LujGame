package lujgame.gateway.network.akka.accept.logic;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import java.util.Map;
import lujgame.gateway.glue.GateGlueActorState;
import lujgame.gateway.network.akka.accept.message.BindForwardRsp;
import lujgame.gateway.network.akka.connection.logic.state.ConnActorState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 绑定网关投递节点
 */
@Component
public class ForwardBinder {

  @Autowired
  public ForwardBinder(ConnKiller connKiller) {
    _connKiller = connKiller;
  }

  public void findForward(GateGlueActorState state,
      String boxId, ActorRef connRef, ActorRef glueRef) {
    // 所有看转发的节点全在这里了，glueActor会根据管理节点的推送来维护

    Map<String, ActorRef> forwardMap = state.getForwardMap();
    ActorRef forwardRef = forwardMap.get(boxId);

    BindForwardRsp rsp = new BindForwardRsp(boxId, forwardRef);
    connRef.tell(rsp, glueRef);
  }

  public void finishBind(ConnActorState state, ActorRef forwardRef,
      String forwardId, ActorRef connRef, LoggingAdapter log) {
    if (forwardRef == null) {
      log.warning("[非法]无效的转发节点绑定 -> {}", forwardId);
      log.warning("连接即将被销毁 -> {}", state.getConnId());

      _connKiller.requestKill(state, connRef);
      return;
    }

    state.setForwardRef(forwardRef);

    //TODO: 是否需要回复客户端？
  }

  private final ConnKiller _connKiller;
}
