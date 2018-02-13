package lujgame.robot.robot.instance.cases;

import io.netty.buffer.ByteBuf;
import lujgame.core.net.PacketHeader;
import lujgame.core.net.ReceiveBuffer;
import lujgame.robot.netty.RobotNettyHandler;
import lujgame.robot.robot.instance.RobotInstanceActor;
import lujgame.robot.robot.instance.RobotInstanceState;
import lujgame.robot.robot.instance.message.Netty2RobotMsg;
import org.springframework.stereotype.Service;

/**
 * 将netty收到的二进制数据解包
 *
 * @see RobotNettyHandler#channelRead
 */
@Service
public class OnNetty2Robot implements RobotInstanceActor.Case<Netty2RobotMsg> {

  //TODO: 测试buf不释放是否会有内存泄漏

  @Override
  public void onHandle(RobotInstanceActor.Context ctx) {
    RobotInstanceState state = ctx.getActorState();
    Netty2RobotMsg msg = ctx.getMessage(this);

  }

  void handleImpl(ByteBuf netBuf, ReceiveBuffer recvBuf) {
    ByteBuf overflowBuf = recvBuf.getOverflowBuf();
    overflowBuf.writeBytes(netBuf);
    netBuf.release();

    //TODO: 拿到头部信息，没有则尝试从overflow中读取
    PacketHeader header = recvBuf.getPendingHeader();
    if (header == null) {
      header = tryDecodeHeader(overflowBuf, recvBuf);
      if (header == null) {
        return;
      }
    }

    //TODO: 根据头部信息，从overflow中读取包体
    int length = header.getLength();
    byte[] bodyData = tryDecodeBody(overflowBuf, length);

    //TODO: 拿到完整包体，解出业务包

    //TODO: 若overflow还有残留，触发下次解包
  }

  private PacketHeader tryDecodeHeader(ByteBuf overflowBuf, ReceiveBuffer recvBuf) {
    if (overflowBuf.readableBytes() < 6) {
      return null;
    }

    Integer opcode = overflowBuf.readMedium();
    int length = overflowBuf.readMedium();

    PacketHeader header = new PacketHeader(opcode, length);
    recvBuf.setPendingHeader(header);

    overflowBuf.discardReadBytes();
    return header;
  }

  private byte[] tryDecodeBody(ByteBuf overflowBuf, int length) {
    if (overflowBuf.readableBytes() < length) {
      return null;
    }

    byte[] data = new byte[length];
    overflowBuf.readBytes(data);

    overflowBuf.discardReadBytes();
    return data;
  }
}
