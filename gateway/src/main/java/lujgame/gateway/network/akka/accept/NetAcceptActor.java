package lujgame.gateway.network.akka.accept;

import akka.actor.ActorRef;
import io.netty.bootstrap.ServerBootstrap;
import lujgame.core.akka.CaseActor;
import lujgame.gateway.network.akka.accept.logic.ConnKiller;
import lujgame.gateway.network.akka.accept.logic.ConnectionItem;
import lujgame.gateway.network.akka.accept.logic.NettyRunner;
import lujgame.gateway.network.akka.accept.logic.NewConnCreator;
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
    log().debug("启动服务器监听。。。");

    ActorRef self = getSelf();
    ServerBootstrap serverBoot = _nettyRunner.createServerBoot(self);

    final int SERVER_PORT = 12345;
    serverBoot.bind(SERVER_PORT);
  }

//  @Override
//  public void onReceive(Object msg) throws Exception {
//    log().debug("aaaaaaaaacept收到消息 -> {}", msg);
//
//    super.onReceive(msg);
//  }

  private void registerMessage() {
    addCase(NewConnMsg.class, this::onNewConn);
    addCase(KillConnMsg.class, this::onKillConn);
  }

  private void onNewConn(NewConnMsg msg) {
    log().debug("accept?????????????????????????");

    NewConnCreator c = _newConnCreator;
    ConnectionItem connectionItem = c.createConnection(this, _state, msg);
    c.addToMap(_state, connectionItem, log());
  }

  private void onKillConn(KillConnMsg msg) {
    String connId = msg.getConnId();
    _connKiller.killConnection(_state, connId, log());
  }

  private final NetAcceptState _state;

  private final NettyRunner _nettyRunner;

  private final NewConnCreator _newConnCreator;
  private final ConnKiller _connKiller;
}
