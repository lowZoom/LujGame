package lujgame.gateway.network.akka.accept.logic;

import akka.actor.ActorRef;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lujgame.core.id.UuidTool;
import lujgame.gateway.network.netty.GateNettyInit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NettyRunner {

  @Autowired
  public NettyRunner(UuidTool uuidTool) {
    _uuidTool = uuidTool;
  }

  public ServerBootstrap createServerBoot(ActorRef acceptRef) {
    ServerBootstrap serverBoot = new ServerBootstrap();

    NioEventLoopGroup bossGroup = new NioEventLoopGroup();
    NioEventLoopGroup workerGroup = new NioEventLoopGroup();

    serverBoot.group(bossGroup)
        .channel(NioServerSocketChannel.class)
        .childHandler(new GateNettyInit(acceptRef, _uuidTool));

    return serverBoot;
  }

  private final UuidTool _uuidTool;
}
