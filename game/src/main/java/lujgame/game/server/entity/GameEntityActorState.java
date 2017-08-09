package lujgame.game.server.entity;

import com.google.common.collect.ImmutableMap;
import lujgame.game.server.net.GameNetHandler;

public class GameEntityActorState {

  public GameEntityActorState(ImmutableMap<Integer, GameNetHandler> netHandlerMap) {
    _netHandlerMap = netHandlerMap;
  }

  public ImmutableMap<Integer, GameNetHandler> getNetHandlerMap() {
    return _netHandlerMap;
  }

  private final ImmutableMap<Integer, GameNetHandler> _netHandlerMap;
}
