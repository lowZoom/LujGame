package lujgame.game.server.net;

public abstract class NetHandleSuite {

  public abstract int getOpcode();

  public abstract GameNetHandler<?> getNetHandler();

  public abstract GameNetCodec getNetCodec();
}
