package lujgame.gateway.network.akka.connection.logic;

import lujgame.gateway.network.akka.connection.logic.packet.ConnPacket;
import lujgame.gateway.network.akka.connection.logic.packet.ConnPacketBuffer;
import lujgame.gateway.network.akka.connection.logic.packet.ConnPacketHeader;
import lujgame.gateway.network.akka.connection.logic.packet.PacketBufferReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PacketBufferDecoder {

  @Autowired
  public PacketBufferDecoder(PacketBufferReader bufferReader) {
    _bufferReader = bufferReader;
  }

  public void decodeHeader(ConnPacketBuffer packetBuf) {
    PacketBufferReader r = _bufferReader;
    if (r.readableBytes(packetBuf) < ConnPacketHeader.HEADER_SIZE) {
      return;
    }

    byte[] numBuf = new byte[3];
    int opcode = r.readMedium(r.readBytes(packetBuf, numBuf));
    int length = r.readMedium(r.readBytes(packetBuf, numBuf));

    ConnPacketHeader header = new ConnPacketHeader(opcode, length);
    packetBuf.setPendingHeader(header);
  }

  public void decodeBody(ConnPacketBuffer packetBuf) {
    PacketBufferReader r = _bufferReader;
    ConnPacketHeader header = packetBuf.getPendingHeader();
    int length = header.getLength();

    if (r.readableBytes(packetBuf) < length) {
      return;
    }

    byte[] data = new byte[length];
    r.readBytes(packetBuf, data);

    ConnPacket packet = new ConnPacket(header.getOpcode(), data);
    packetBuf.setPendingPacket(packet);
  }

  public void finishDecode(ConnPacketBuffer packetBuf) {
    packetBuf.setPendingHeader(null);
    packetBuf.setPendingPacket(null);
  }

  public boolean isHeaderOk(ConnPacketBuffer packetBuf) {
    return packetBuf.getPendingHeader() != null;
  }

  public boolean isPacketOk(ConnPacketBuffer packetBuf) {
    return packetBuf.getPendingPacket() != null;
  }

  private final PacketBufferReader _bufferReader;
}
