package lujgame.gateway.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import java.nio.charset.StandardCharsets;

public class GatePayloadHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    try {
      ByteBuf dataBuf = (ByteBuf) msg;
      String str = dataBuf.toString(StandardCharsets.UTF_8);
      System.out.println("ppppppayload-============> " + str);
    } finally {
      ReferenceCountUtil.release(msg);
    }
  }
}
