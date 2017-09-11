package lujgame.game.server;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActorContext;
import akka.cluster.ClusterEvent;
import akka.cluster.Member;
import akka.event.LoggingAdapter;
import lujgame.core.akka.AkkaTool;
import lujgame.core.akka.common.CaseActor;
import lujgame.game.boot.message.BootFailMsg;
import lujgame.game.master.cluster.GameNodeRegistrar;
import lujgame.game.server.database.cache.DbCacheActorFactory;
import lujgame.game.server.entity.logic.EntityBinder;
import lujgame.gateway.network.akka.accept.message.BindForwardReqRemote;

public class GameServerActor extends CaseActor {

  public GameServerActor(
      GameServerActorState state,
      AkkaTool akkaTool,
      GameNodeRegistrar serverRegistrar,
      EntityBinder entityBinder,
      DbCacheActorFactory dbCacheActorFactory) {
    _state = state;

    _akkaTool = akkaTool;
    _serverRegistrar = serverRegistrar;

    _entityBinder = entityBinder;
    _dbCacheActorFactory = dbCacheActorFactory;

    addCase(BootFailMsg.class, this::onBootFail);
    addCase(BindForwardReqRemote.class, this::onBindForward);
  }

  @Override
  public void preStart() throws Exception {
    LoggingAdapter log = log();

    GameServerActorState state = _state;
    log.debug("启动Akka系统完成，服务器ID：{}", state.getServerId());

    _akkaTool.subscribeClusterMemberUp(state.getCluster(), this, this::onMemberUp);

    startDatabase(state, getContext());
  }

  private void startDatabase(GameServerActorState state, UntypedActorContext ctx) {
    Props props = _dbCacheActorFactory.props(state.getServerConfig().getConfig("database"));
    ActorRef dbCacheRef = ctx.actorOf(props);
    state.setDbCacheRef(dbCacheRef);
  }

  private void onBootFail(BootFailMsg msg) {
    LoggingAdapter log = log();

    log.error("服务器启动失败，原因如下：");
    log.error(msg.getMessage());

    log.info("服务器开始关闭...");
    UntypedActorContext ctx = getContext();
    ctx.stop(getSelf());

    ActorSystem system = ctx.system();
    system.terminate();
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
  private final DbCacheActorFactory _dbCacheActorFactory;
}
