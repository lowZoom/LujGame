package lujgame.game.server.net;

public abstract class GameNetHandler<P> {

  public abstract String desc();

  public abstract void onHandle(GameNetHandleContext ctx);
}
