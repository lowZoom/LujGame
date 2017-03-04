package lujgame.gateway.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lujgame.core.akka.CaseActor;
import lujgame.gateway.network.netty.ChildInboundHandler;

public class NetAcceptActor extends CaseActor {

  @Override
  public void preStart() throws Exception {
    log().debug("启动服务器监听。。。");

    ServerBootstrap serverBoot = new ServerBootstrap();

    NioEventLoopGroup boss = new NioEventLoopGroup();
    NioEventLoopGroup worker = new NioEventLoopGroup();

    serverBoot.group(boss)
        .channel(NioServerSocketChannel.class)
        .childHandler(new ChildInboundHandler());

    final int SERVER_PORT = 12345;
    serverBoot.bind(SERVER_PORT);
  }
}
