package lujgame.game.server.net.packet;

import lujgame.game.server.core.LujInternal;

@LujInternal
public class Packetimpl0 {

  public NetPacketCodec getCodec(PacketImpl<?> packet) {
    return packet._codec;
  }

  public void setCodec(PacketImpl<?> packet, NetPacketCodec codec) {
    packet._codec = codec;
  }

  public <T> T getValue(PacketImpl<T> packet) {
    return packet._value;
  }
}
