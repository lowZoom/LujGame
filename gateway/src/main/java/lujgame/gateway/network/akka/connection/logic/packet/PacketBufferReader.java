package lujgame.gateway.network.akka.connection.logic.packet;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PacketBufferReader {

  public int readableBytes(ConnPacketBuffer buf) {
    List<byte[]> bufferList = buf.getBufferList();
    int readIndex = buf.getReadIndex();

    int cursor = 0;
    int dataIndex = 0;

    // 先找出读到哪个数组
    for (int i = 0, n = bufferList.size(); i < n; i++) {
      byte[] data = bufferList.get(i);

      cursor += data.length;
      dataIndex = i;

      if (cursor >= readIndex) {
        break;
      }
    }

    // 从该数组累加到结束就是结果
    int result = cursor - readIndex;
    for (int i = dataIndex + 1, n = bufferList.size(); i < n; i++) {
      byte[] data = bufferList.get(i);
      result += data.length;
    }

    return result;
  }
}
