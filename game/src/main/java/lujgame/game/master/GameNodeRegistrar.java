package lujgame.game.master;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Address;
import akka.actor.UntypedActorContext;
import akka.cluster.Member;
import akka.event.LoggingAdapter;
import java.util.Map;
import lujgame.game.master.message.GNodeRegMsg;
import org.springframework.stereotype.Component;

@Component
public class GameNodeRegistrar {

  public boolean isMaster(Member member) {
    return member.roles().contains("seed");
  }

  public void requestRegister(Member masterInfo, String serverId,
      ActorRef nodeRef, UntypedActorContext ctx) {
    Address addr = masterInfo.address();
    ActorSelection masterSelect = ctx.actorSelection(addr + "/user/Master");

    GNodeRegMsg msg = new GNodeRegMsg(serverId);
    masterSelect.tell(msg, nodeRef);
  }

  public void addServerNode(ClusterBossActorState state,
      String serverId, ActorRef nodeRef, LoggingAdapter log) {
    Map<String, ActorRef> nodeMap = state.getGameNodeMap();
    nodeMap.put(serverId, nodeRef);

    log.info("注册新的游戏服节点 -> {}", serverId);
    log.info("当前游戏服总数 -> {}", nodeMap.size());
  }
}
