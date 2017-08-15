package lujgame.game.server;

import akka.cluster.ClusterEvent;
import akka.cluster.Member;
import akka.event.LoggingAdapter;
import com.google.common.collect.ImmutableMap;
import lujgame.core.akka.AkkaTool;
import lujgame.core.akka.common.CaseActor;
import lujgame.game.master.cluster.GameNodeRegistrar;
import lujgame.game.server.entity.logic.EntityBinder;
import lujgame.game.server.net.NetHandleSuite;
import lujgame.gateway.network.akka.accept.message.BindForwardReqRemote;

public class GameServerActor extends CaseActor {

  public GameServerActor(
      GameServerActorState state,
      AkkaTool akkaTool,
      GameNodeRegistrar serverRegistrar,
      EntityBinder entityBinder) {
    _state = state;

    _akkaTool = akkaTool;
    _serverRegistrar = serverRegistrar;

    _entityBinder = entityBinder;

    addCase(BindForwardReqRemote.class, this::onBindForward);
  }

  @Override
  public void preStart() throws Exception {
    LoggingAdapter log = log();

    GameServerActorState state = _state;
    log.debug("游戏服启动，ID：{}", state.getServerId());

    ImmutableMap<Integer, NetHandleSuite> handleSuiteMap = state.getHandleSuiteMap();
    log.debug("网络包处理器数量 -> {}", handleSuiteMap.size());

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

  private void onBindForward(BindForwardReqRemote msg) {
    _entityBinder.bindEntity(_state, msg.getConnId(), getContext(), getSender(), getSelf());
  }

  private final GameServerActorState _state;

  private final AkkaTool _akkaTool;
  private final GameNodeRegistrar _serverRegistrar;

  private final EntityBinder _entityBinder;
}
