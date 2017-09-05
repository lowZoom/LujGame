package lujgame.game.server.entity;

import akka.actor.ActorRef;
import com.google.common.collect.ImmutableMap;
import lujgame.game.server.net.NetHandleSuite;

public class GameEntityActorState {

  public GameEntityActorState(
      ImmutableMap<Integer, NetHandleSuite> handleSuiteMap,
      ActorRef dbCacheRef) {
    _handleSuiteMap = handleSuiteMap;
    _dbCacheRef = dbCacheRef;
  }

  public ImmutableMap<Integer, NetHandleSuite> getHandleSuiteMap() {
    return _handleSuiteMap;
  }

  public ActorRef getDbCacheRef() {
    return _dbCacheRef;
  }

  private final ImmutableMap<Integer, NetHandleSuite> _handleSuiteMap;

  private final ActorRef _dbCacheRef;
}
