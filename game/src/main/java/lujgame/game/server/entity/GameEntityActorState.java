package lujgame.game.server.entity;

import lujgame.game.server.net.NetHandlerMap;

public class GameEntityActorState {

  public GameEntityActorState(NetHandlerMap netHandlerMap) {
    _netHandlerMap = netHandlerMap;
  }

  public NetHandlerMap getNetHandlerMap() {
    return _netHandlerMap;
  }

  private final NetHandlerMap _netHandlerMap;
}
