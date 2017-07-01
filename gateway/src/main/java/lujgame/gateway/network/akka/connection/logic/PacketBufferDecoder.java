package lujgame.gateway.network.akka.connection.logic;

import lujgame.gateway.network.akka.connection.logic.packet.ConnPacketBuffer;
import lujgame.gateway.network.akka.connection.logic.packet.ConnPacketHeader;
import org.springframework.stereotype.Component;

@Component
public class PacketBufferDecoder {

  public boolean hasPacketToDecode(ConnPacketBuffer packetBuf) {
    ConnPacketHeader header = packetBuf.getPendingHeader();
//    if (header == null) {
//      return
//    }
//
//    List<byte[]> bufList = packetBuf.getBufferList();
    return false;
  }
}
