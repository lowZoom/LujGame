package lujgame.game.server.net;

public abstract class NetPacketCodec {

  public abstract Class<?> packetType();

  public abstract Object decode(byte[] data);
}
