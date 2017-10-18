package lujgame.gateway.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lujgame.gateway.network.netty.event.NettySendEvent;

public class GateNettyEncoder extends MessageToByteEncoder<NettySendEvent> {

  @Override
  protected void encode(ChannelHandlerContext ctx,
      NettySendEvent msg, ByteBuf out) throws Exception {
    byte[] data = msg.getData();

    out.writeMedium(msg.getOpcode());
    out.writeMedium(data.length);

    out.writeBytes(data);
  }
}
