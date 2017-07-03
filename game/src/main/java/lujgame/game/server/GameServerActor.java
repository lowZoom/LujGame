package lujgame.game.server;

import akka.cluster.ClusterEvent;
import akka.cluster.Member;
import lujgame.core.akka.AkkaTool;
import lujgame.core.akka.CaseActor;
import lujgame.game.master.GameNodeRegistrar;

public class GameServerActor extends CaseActor {

  public GameServerActor(
      GameServerActorState state,
      AkkaTool akkaTool,
      GameNodeRegistrar serverRegistrar) {
    _state = state;

    _akkaTool = akkaTool;
    _serverRegistrar = serverRegistrar;
  }

  @Override
  public void preStart() throws Exception {
    GameServerActorState state = _state;
    log().debug("游戏服启动，ID：{}", state.getServerId());

    _akkaTool.subscribeClusterMemberUp(state.getCluster(), this, this::onMemberUp);
  }

  /**
   * 将本服注册到管理节点上
   */
  private void onMemberUp(ClusterEvent.MemberUp msg) {
    GameNodeRegistrar r = _serverRegistrar;
    Member member = msg.member();
    if (r.isMaster(member)) {
      r.requestRegister(member, _state.getServerId(), getSelf(), getContext());
    }
  }

  private final GameServerActorState _state;

  private final AkkaTool _akkaTool;
  private final GameNodeRegistrar _serverRegistrar;
}
