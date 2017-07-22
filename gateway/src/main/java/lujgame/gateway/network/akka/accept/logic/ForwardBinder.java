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

  /**
   * 增加新的转发节点
   */
  public void addForward(GateGlueActorState state,
      String forwardId, ActorRef forwardRef, LoggingAdapter log) {
    log.info("新的转发节点：{} -> {}", forwardId, forwardRef.path());

    Map<String, ActorRef> forwardMap = state.getForwardMap();
    forwardMap.put(forwardId, forwardRef);
  }

  /**
   * @param forwardId 在glueActor中查询此id对应转发节点
   */
  public void findForward(GateGlueActorState state,
      String forwardId, ActorRef connRef, ActorRef glueRef) {
    Map<String, ActorRef> forwardMap = state.getForwardMap();
    ActorRef forwardRef = forwardMap.get(forwardId);

    BindForwardRsp rsp = new BindForwardRsp(forwardId, forwardRef);
    connRef.tell(rsp, glueRef);
  }

  /**
   * 在connActor中结束绑定
   */
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
