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

  public ByteBuf getOverflowBuf() {
    return _overflowBuf;
  }

  private PacketHeader _pendingHeader;

//  private final ByteBuf _headerBuf;
//
//  private final ByteBuf _bodyBuf;

  private final ByteBuf _overflowBuf;
}
