package lujgame.core.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ReceiveBuffer {

  public ReceiveBuffer() {
    _overflowBuf = Unpooled.buffer();
  }

  public PacketHeader getPendingHeader() {
    return _pendingHeader;
  }

  public void setPendingHeader(PacketHeader pendingHeader) {
    _pendingHeader = pendingHeader;
  }

  public byte[] getPendingBody() {
    return _pendingBody;
  }

  public void setPendingBody(byte[] pendingBody) {
    _pendingBody = pendingBody;
  }

  public ByteBuf getOverflowBuf() {
    return _overflowBuf;
  }

  private PacketHeader _pendingHeader;
  private byte[] _pendingBody;

  private final ByteBuf _overflowBuf;
}
