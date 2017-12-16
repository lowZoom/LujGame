package lujgame.gateway.network.akka.accept.logic.bind;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import java.util.Map;
import lujgame.gateway.glue.GateGlueActorState;
import lujgame.gateway.network.akka.accept.message.BindForwardReqRemote;
import org.springframework.stereotype.Service;

/**
 * 绑定网关投递节点
 */
@Service
public class ForwardBinder {

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
      String forwardId, String connId, ActorRef connRef) {
    Map<String, ActorRef> forwardMap = state.getForwardMap();
    ActorRef forwardBossRef = forwardMap.get(forwardId);

    //TODO: 判断bossRef为null的情况

    // 将绑定请求转发给游戏服
    BindForwardReqRemote req = new BindForwardReqRemote(connId);
    forwardBossRef.tell(req, connRef);
  }
}
