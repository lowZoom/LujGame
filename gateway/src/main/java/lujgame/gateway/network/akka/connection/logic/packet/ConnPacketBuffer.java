package lujgame.gateway.network.akka.connection.logic.packet;

import java.util.ArrayList;
import java.util.List;

public class ConnPacketBuffer {

  public ConnPacketBuffer() {
    _bufferList = new ArrayList<>(64);
  }

  public int getReadIndex() {
    return _readIndex;
  }

  public void setReadIndex(int readIndex) {
    _readIndex = readIndex;
  }

  public ConnPacketHeader getPendingHeader() {
    return _pendingHeader;
  }

  public void setPendingHeader(
      ConnPacketHeader pendingHeader) {
    _pendingHeader = pendingHeader;
  }

  public ConnPacket getPendingPacket() {
    return _pendingPacket;
  }

  public void setPendingPacket(
      ConnPacket pendingPacket) {
    _pendingPacket = pendingPacket;
  }

  public List<byte[]> getBufferList() {
    return _bufferList;
  }

  private int _readIndex;

  private ConnPacketHeader _pendingHeader;
  private ConnPacket _pendingPacket;

  private final List<byte[]> _bufferList;
}
