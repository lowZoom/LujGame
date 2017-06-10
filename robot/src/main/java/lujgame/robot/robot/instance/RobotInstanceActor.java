package lujgame.robot.robot.instance;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import lujgame.core.akka.CaseActor;

/**
 * 代表一个机器人
 */
public class RobotInstanceActor extends CaseActor {

  public RobotInstanceActor(EventLoopGroup workerGroup, String ip, int port) {
    _workerGroup = workerGroup;

    _ip = ip;
    _port = port;
  }

  @Override
  public void preStart() throws Exception {
    log().info("检测目标服务器 -> {}:{}", _ip, _port);

    //TODO: 只创一个，整个APP重用
    new Bootstrap()
        .group(_workerGroup)
        .channel(NioSocketChannel.class)
//        .option(ChannelOption.SO_KEEPALIVE, true)
        .handler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel socketChannel) throws Exception {

          }
        })
        .connect(_ip, _port)
        .addListener(this::onConnectDone);
  }

  private void onConnectDone(Future<? super Void> future) {
    log().debug("连接结果：{}", future.isSuccess());
  }

  private final EventLoopGroup _workerGroup;

  private final String _ip;
  private final int _port;
}
