package lujgame.game.master;

import lujgame.core.akka.AkkaTool;
import lujgame.core.akka.CaseActor;
import lujgame.game.master.message.GNodeRegMsg;

public class ClusterBossActor extends CaseActor {

  public ClusterBossActor(
      ClusterBossActorState state,
      AkkaTool akkaTool,
      GameNodeRegistrar nodeRegistrar) {
    _state = state;

    _akkaTool = akkaTool;
    _nodeRegistrar = nodeRegistrar;

    addCase(GNodeRegMsg.class, this::onNodeReg);
  }

  @Override
  public void preStart() throws Exception {
    log().debug("游戏服管理中心启动...");
  }

  private void onNodeReg(GNodeRegMsg msg) {
    _nodeRegistrar.addServerNode(_state, msg.getServerId(), getSender(), log());
  }

  private final ClusterBossActorState _state;

  private final AkkaTool _akkaTool;
  private final GameNodeRegistrar _nodeRegistrar;
}
