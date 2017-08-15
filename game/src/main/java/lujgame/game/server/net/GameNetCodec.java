package lujgame.game.server.net;

public abstract class GameNetCodec {

  public abstract Object decode(byte[] data);
}
