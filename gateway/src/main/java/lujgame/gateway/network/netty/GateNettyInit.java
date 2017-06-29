package lujgame.gateway.network.netty;

import akka.actor.ActorRef;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
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
    pipeline.addLast(new GateNettyHandler(_acceptRef, _uuidTool));

    LengthFieldBasedFrameDecoder payloadDecoder = new LengthFieldBasedFrameDecoder(
        1024, 0, 3, 0, 3);

    GatePayloadHandler payloadHandler = new GatePayloadHandler();

    pipeline.addLast("decode", new FixedLengthFrameDecoder(3));
    pipeline.addLast("handle", new GateOpcodeDecoder(payloadDecoder, payloadHandler));
  }

  private final ActorRef _acceptRef;

  private final UuidTool _uuidTool;
}
