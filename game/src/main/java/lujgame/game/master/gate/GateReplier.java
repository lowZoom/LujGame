package lujgame.game.master.gate;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import java.util.Map;
import lujgame.game.master.cluster.ClusterBossActorState;
import lujgame.gateway.glue.message.NewForwardMsg;
import org.springframework.stereotype.Component;

@Component
public class GateReplier {

  public void replyGate(ClusterBossActorState state,
      ActorRef gateRef, ActorRef commRef, LoggingAdapter log) {
    Map<String, ActorRef> nodeMap = state.getGameNodeMap();
    log.info("向网关同步游戏服集群信息，节点数量：{}", nodeMap.size());

    for (Map.Entry<String, ActorRef> e : nodeMap.entrySet()) {
      String gameId = e.getKey();
      ActorRef gameRef = e.getValue();

      NewForwardMsg msg = new NewForwardMsg(gameId, gameRef);
      gateRef.tell(msg, commRef);
    }
  }
}
