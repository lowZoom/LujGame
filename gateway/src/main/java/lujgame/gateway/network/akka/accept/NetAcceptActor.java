package lujgame.gateway.network.akka.accept;

import akka.event.LoggingAdapter;
import lujgame.core.akka.common.CaseActor;
import lujgame.gateway.network.akka.accept.logic.ConnKiller;
import lujgame.gateway.network.akka.accept.logic.ConnectionItem;
import lujgame.gateway.network.akka.accept.logic.NettyRunner;
import lujgame.gateway.network.akka.accept.logic.NewConnCreator;
import lujgame.gateway.network.akka.accept.message.BindForwardReqLocal;
import lujgame.gateway.network.akka.accept.message.KillConnMsg;
import lujgame.gateway.network.akka.accept.message.NewConnMsg;

public class NetAcceptActor extends CaseActor {

  public NetAcceptActor(
      NetAcceptState state,
      NettyRunner nettyRunner,
      NewConnCreator newConnCreator,
      ConnKiller connKiller) {
    _state = state;
    _nettyRunner = nettyRunner;

    _newConnCreator = newConnCreator;
    _connKiller = connKiller;

    //-- 注册消息处理 --//
    registerMessage();
  }

  @Override
  public void preStart() throws Exception {
    LoggingAdapter log = log();
    log.debug("启动游戏服网络监听...");

    _nettyRunner.startBind(_state, getSelf(), log);
  }

  private void registerMessage() {
    addCase(NewConnMsg.class, this::onNewConn);
    addCase(KillConnMsg.class, this::onKillConn);

    addCase(BindForwardReqLocal.class, this::onBindForward);
  }

  private void onNewConn(NewConnMsg msg) {
//    log().debug("accept?????????????????????????");

    NewConnCreator c = _newConnCreator;
    ConnectionItem connectionItem = c.createConnection(this, _state, msg);
    c.addToMap(_state, connectionItem, log());
  }

  private void onKillConn(KillConnMsg msg) {
    _connKiller.killConnection(_state, msg.getConnId(), getContext(), log());
  }

  private void onBindForward(BindForwardReqLocal msg) {
    _state.getGlueRef().forward(msg, getContext());
  }

  private final NetAcceptState _state;

  private final NettyRunner _nettyRunner;

  private final NewConnCreator _newConnCreator;
  private final ConnKiller _connKiller;
}
