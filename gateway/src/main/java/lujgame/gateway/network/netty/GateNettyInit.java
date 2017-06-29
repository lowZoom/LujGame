package lujgame.gateway.network.netty;

import akka.actor.ActorRef;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lujgame.core.id.UuidTool;

public class GateNettyInit extends ChannelInitializer<SocketChannel> {

  public GateNettyInit(
      ActorRef acceptRef,
      UuidTool uuidTool) {
    _acceptRef = acceptRef;
    _uuidTool = uuidTool;
  }

  @Override
  protected void initChannel(SocketChannel channel) throws Exception {
    ChannelPipeline pipeline = channel.pipeline();
    pipeline.addLast(new GateNettyConn(_acceptRef, _uuidTool));
  }

  private final ActorRef _acceptRef;
  private final UuidTool _uuidTool;
}
