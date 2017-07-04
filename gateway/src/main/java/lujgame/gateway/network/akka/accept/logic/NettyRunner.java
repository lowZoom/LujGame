package lujgame.gateway.network.akka.accept.logic;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lujgame.gateway.network.akka.accept.NetAcceptState;
import lujgame.gateway.network.netty.GateNettyInit;
import org.springframework.stereotype.Component;

@Component
public class NettyRunner {

  public void startBind(NetAcceptState state, ActorRef acceptRef, LoggingAdapter log) {
    String host = state.getHost();
    int port = state.getPort();
    log.debug("绑定 -> {}:{}", host, port);

    NioEventLoopGroup bossGroup = new NioEventLoopGroup();
    NioEventLoopGroup workerGroup = new NioEventLoopGroup();

    new ServerBootstrap().group(bossGroup)
        .channel(NioServerSocketChannel.class)
        .childHandler(new GateNettyInit(acceptRef))
        .bind(host, port);
  }
}
