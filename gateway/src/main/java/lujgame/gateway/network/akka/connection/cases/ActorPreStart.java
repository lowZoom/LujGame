package lujgame.gateway.network.akka.connection.cases;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.LoggingAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import java.net.InetSocketAddress;
import javax.inject.Inject;
import lujgame.gateway.network.akka.connection.GateConnActor;
import lujgame.gateway.network.akka.connection.logic.ConnInfoGetter;
import lujgame.gateway.network.akka.connection.logic.DumbDetector;
import lujgame.gateway.network.akka.connection.logic.state.ConnActorState;
import lujgame.gateway.network.netty.event.NettyConnEvent;
import org.springframework.stereotype.Service;

@Service
class ActorPreStart implements GateConnActor.PreStart {

  @Override
  public void preStart(GateConnActor.Context ctx) throws Exception {
    ConnActorState state = ctx.getActorState();
    InetSocketAddress remoteAddr = _connInfoGetter.getRemoteAddress(state);

    LoggingAdapter log = ctx.getActorLogger();
    log.info("新连接 -> {}", remoteAddr);

    UntypedActor connActor = ctx.getActor();
    ActorRef connRef = connActor.getSelf();
    updateNettyHandler(state, connRef);

    // 启动空连接检测
//    _dumbDetector.startDetect(connActor);
  }

  private void updateNettyHandler(ConnActorState state, ActorRef connRef) {
    ChannelHandlerContext nettyCtx = state.getNettyContext();
    System.out.println("packet -->> conn event~!!!! -> " + nettyCtx);

    ChannelPipeline pipeline = nettyCtx.pipeline();
    NettyConnEvent event = new NettyConnEvent(connRef);
    pipeline.fireUserEventTriggered(event);
  }

  @Inject
  private ConnInfoGetter _connInfoGetter;

  @Inject
  private DumbDetector _dumbDetector;
}
