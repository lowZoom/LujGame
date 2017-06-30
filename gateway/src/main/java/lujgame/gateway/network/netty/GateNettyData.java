package lujgame.gateway.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lujgame.gateway.network.akka.connection.message.ConnDataMsg;

public abstract class GateNettyData extends ChannelInboundHandlerAdapter {

  public abstract void onDataMsg(ConnDataMsg msg);

  @Override
  public final void channelRead(ChannelHandlerContext ctx, Object bufObj) throws Exception {
    try {
      ByteBuf buf = (ByteBuf) bufObj;
      byte[] data = new byte[buf.readableBytes()];
      buf.readBytes(data);

      ConnDataMsg msg = new ConnDataMsg(data);
      onDataMsg(msg);
    } finally {
      ReferenceCountUtil.release(bufObj);
    }
  }
}
