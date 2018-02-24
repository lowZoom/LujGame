package lujgame.robot.robot.instance.control.packet;

import io.netty.buffer.ByteBuf;
import lujgame.core.net.PacketHeader;
import lujgame.core.net.ReceiveBuffer;
import org.springframework.stereotype.Service;

@Service
public class PacketReceiver {

  public void receive(ByteBuf newData, ReceiveBuffer recvBuf) {
    ByteBuf overflowBuf = recvBuf.getOverflowBuf();
    overflowBuf.writeBytes(newData);

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
    if (bodyData == null) {
      return;
    }

    //TODO: 拿到完整包体，解出业务包
    recvBuf.setPendingBody(bodyData);
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
