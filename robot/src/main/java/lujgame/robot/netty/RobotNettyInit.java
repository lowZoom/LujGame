package lujgame.robot.netty;

import akka.actor.ActorRef;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class RobotNettyInit extends ChannelInitializer<SocketChannel> {

  public RobotNettyInit(ActorRef instanceRef) {
    _instanceRef = instanceRef;
  }

  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();
    pipeline.addLast(new RobotNettyEncoder());

    // 需要放在最后，才能传递到outbound
    pipeline.addLast(new RobotNettyHandler(_instanceRef));
  }

  private final ActorRef _instanceRef;
}
