package lujgame.gateway.network.netty;

import akka.actor.ActorRef;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lujgame.core.id.UuidTool;

public class GateNettyInit extends ChannelInitializer<SocketChannel> {

  public GateNettyInit(
      ActorRef acceptRef) {
    _acceptRef = acceptRef;
  }

  @Override
  protected void initChannel(SocketChannel channel) throws Exception {
    ChannelPipeline pipeline = channel.pipeline();
    pipeline.addLast(new GateNettyConn(_acceptRef));
  }

  private final ActorRef _acceptRef;
}
