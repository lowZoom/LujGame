package lujgame.game.server.net.packet;

public abstract class PacketImpl<T> {

  protected PacketImpl(T value) {
    _value = value;
  }

  NetPacketCodec _codec;

  final T _value;
}
