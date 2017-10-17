package lujgame.game.server.net.packet;

public abstract class PacketImpl {

  protected PacketImpl(NetPacketCodec codec) {
    _codec = codec;
  }

  final NetPacketCodec _codec;
}
