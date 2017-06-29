package lujgame.gateway.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.ReferenceCountUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class GateOpcodeDecoder extends ChannelInboundHandlerAdapter {

  @Autowired
  public GateOpcodeDecoder(LengthFieldBasedFrameDecoder payloadDecoder,
      GatePayloadHandler payloadHandler) {
    _payloadDecoder = payloadDecoder;
    _payloadHandler = payloadHandler;
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    try {
      ByteBuf dataBuf = (ByteBuf) msg;

      Integer opcode = dataBuf.readUnsignedMedium();
      System.out.println("ooooooopcode------------> " + opcode);

      ChannelPipeline pipeline = ctx.pipeline();
      pipeline.replace("decode", "decode", _payloadDecoder);
      pipeline.replace("handle", "handle", _payloadHandler);
    } finally {
      ReferenceCountUtil.release(msg);
    }
  }

  private final LengthFieldBasedFrameDecoder _payloadDecoder;
  private final GatePayloadHandler _payloadHandler;
}
