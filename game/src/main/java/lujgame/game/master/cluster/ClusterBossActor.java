package lujgame.game.master.cluster;

import lujgame.core.akka.common.CaseActor;
import lujgame.game.master.cluster.message.GNodeRegMsg;
import lujgame.game.master.cluster.message.ReplyGateMsg;
import lujgame.game.master.gate.GateReplier;

public class ClusterBossActor extends CaseActor {

  public ClusterBossActor(
      ClusterBossActorState state,
      GameNodeRegistrar nodeRegistrar,
      GateReplier gateReplier) {
    _state = state;

    _gateReplier = gateReplier;
    _nodeRegistrar = nodeRegistrar;

    addCase(GNodeRegMsg.class, this::onNodeReg);
    addCase(ReplyGateMsg.class, this::onReplyGate);
  }

  @Override
  public void preStart() throws Exception {
    log().debug("游戏服管理中心启动...");
  }

  private void onNodeReg(GNodeRegMsg msg) {
    _nodeRegistrar.addServerNode(_state, msg.getServerId(), getSender(), log());
  }

  private void onReplyGate(ReplyGateMsg msg) {
    _gateReplier.replyGate(_state, msg.getGateRef(), getSender(), log());
  }

  private final ClusterBossActorState _state;

  private final GameNodeRegistrar _nodeRegistrar;
  private final GateReplier _gateReplier;
}
