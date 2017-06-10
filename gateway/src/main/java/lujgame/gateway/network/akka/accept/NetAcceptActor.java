package lujgame.gateway.network.akka.accept;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActorContext;
import io.netty.bootstrap.ServerBootstrap;
import java.net.InetSocketAddress;
import lujgame.core.akka.CaseActor;
import lujgame.gateway.network.akka.accept.logic.NettyRunner;
import lujgame.gateway.network.akka.accept.message.NewConnMsg;
import lujgame.gateway.network.akka.connection.ConnActorFactory;

public class NetAcceptActor extends CaseActor {

  public NetAcceptActor(
      NetAcceptState state,
      NettyRunner nettyRunner,
      ConnActorFactory connActorFactory) {
    _state = state;

    _nettyRunner = nettyRunner;
    _connActorFactory = connActorFactory;

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

  private void registerMessage() {
    addCase(NewConnMsg.class, this::onNewConn);
  }

  private void onNewConn(NewConnMsg msg) {
    ConnActorFactory connFactory = _connActorFactory;
    String connId = msg.getConnId();
    InetSocketAddress remoteAddr = msg.getRemoteAddr();

    UntypedActorContext ctx = getContext();
    Props props = connFactory.props(connId, remoteAddr);
    String name = connFactory.getActorName(connId);
    ctx.actorOf(props, name);

    //TODO: 加入map进行管理
  }

  private final NetAcceptState _state;

  private final NettyRunner _nettyRunner;
  private final ConnActorFactory _connActorFactory;
}
