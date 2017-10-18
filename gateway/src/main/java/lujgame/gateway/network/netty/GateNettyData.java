package lujgame.gateway.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lujgame.gateway.network.akka.connection.message.Netty2GateMsg;

public abstract class GateNettyData extends ChannelInboundHandlerAdapter {

  public abstract void onDataMsg(Netty2GateMsg msg);

  @Override
  public final void channelRead(ChannelHandlerContext ctx, Object bufObj) throws Exception {
    try {
      ByteBuf buf = (ByteBuf) bufObj;
      byte[] data = new byte[buf.readableBytes()];
      buf.readBytes(data);

      Netty2GateMsg msg = new Netty2GateMsg(data);
      onDataMsg(msg);
    } finally {
      ReferenceCountUtil.release(bufObj);
    }
  }
}
