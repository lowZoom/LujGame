package lujgame.game.server.entity;

import com.google.common.collect.ImmutableMap;
import lujgame.game.server.net.NetHandleSuite;

public class GameEntityActorState {

  public GameEntityActorState(
      ImmutableMap<Integer, NetHandleSuite> handleSuiteMap) {
    _handleSuiteMap = handleSuiteMap;
  }

  public ImmutableMap<Integer, NetHandleSuite> getHandleSuiteMap() {
    return _handleSuiteMap;
  }

  private final ImmutableMap<Integer, NetHandleSuite> _handleSuiteMap;
}
