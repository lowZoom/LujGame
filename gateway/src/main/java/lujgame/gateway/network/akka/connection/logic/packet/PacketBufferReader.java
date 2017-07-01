package lujgame.gateway.network.akka.connection.logic.packet;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PacketBufferReader {

  public int readableBytes(ConnPacketBuffer buf) {
    List<byte[]> bufferList = buf.getBufferList();
    if (bufferList.isEmpty()) {
      return 0;
    }

    int readIndex = buf.getReadIndex();
    int cursor = 0;
    int dataIndex = 0;

    // 先找出统计起点
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

  public byte[] readBytes(ConnPacketBuffer buf, byte[] out) {
    List<byte[]> bufferList = buf.getBufferList();
    int readIndex = buf.getReadIndex();
    int cursor = 0;
    int dataIndex = 0;

    // 先找出读取起点
    for (int i = 0, n = bufferList.size(); i < n; i++) {
      byte[] data = bufferList.get(i);

      cursor += data.length;
      dataIndex = i;

      if (cursor >= readIndex) {
        break;
      }
    }

    // 算出起点数组的下标
    byte[] curArray = bufferList.get(dataIndex);
    int lastCursor = cursor - curArray.length;
    int startIndex = readIndex - lastCursor;

    // 开始复制
    int outIndex = 0;
    int outEnd = out.length;

    for (int i = dataIndex, n = bufferList.size(); i < n; i++) {
      byte[] data = bufferList.get(i);

      for (int j = startIndex, m = data.length; j < m; j++) {
        out[outIndex] = data[j];
        outIndex++;

        if (outIndex >= outEnd) {
          buf.setReadIndex(readIndex + out.length);
          return out;
        }
      }

      startIndex = 0;
    }

    throw new RuntimeException("请先判断readableBytes再读取");
  }

  public int readMedium(byte[] data) {
    return ((data[0] & 0xFF) << 16) | ((data[1] & 0xFF) << 8) | data[2] & 0xFF;
  }

  //TODO: GC
}
