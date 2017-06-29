package lujgame.robot.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToByteEncoder;

public class RobotNettyEncoder extends MessageToByteEncoder<RobotNetPacket> {

  @Override
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
      throws Exception {
    System.out.println(Thread.currentThread() + " write?????????????????");
    super.write(ctx, msg, promise);
  }

  @Override
  protected void encode(ChannelHandlerContext ctx,
      RobotNetPacket packet, ByteBuf out) throws Exception {
    System.out.println(Thread.currentThread() + " encode!!!!!!!!!!");

    int opcode = packet.getOpcode();
    byte[] data = packet.getData();

    out.writeMedium(opcode);
    out.writeMedium(data.length);
    out.writeBytes(data);
  }
}
