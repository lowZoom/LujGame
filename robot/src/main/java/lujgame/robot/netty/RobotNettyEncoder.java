package lujgame.robot.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RobotNettyEncoder extends MessageToByteEncoder<RobotNetPacket> {

  @Override
  protected void encode(ChannelHandlerContext ctx,
      RobotNetPacket packet, ByteBuf out) throws Exception {
    int opcode = packet.getOpcode();
    byte[] data = packet.getData();

//    System.out.println(Thread.currentThread() + " encode!!!!!!!!!! opcode -> " + opcode);

    out.writeMedium(opcode);
    out.writeMedium(data.length);
    out.writeBytes(data);
  }
}
