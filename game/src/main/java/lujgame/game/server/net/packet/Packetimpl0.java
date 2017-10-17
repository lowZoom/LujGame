package lujgame.game.server.net.packet;

import lujgame.game.server.core.LujInternal;

@LujInternal
public class Packetimpl0 {

  public NetPacketCodec getCodec(PacketImpl packet) {
    return packet._codec;
  }
}
