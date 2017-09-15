package lujgame.game.server.entity;

import akka.actor.ActorRef;
import com.google.common.collect.ImmutableMap;
import lujgame.game.server.command.CacheOkCommand;
import lujgame.game.server.net.NetHandleSuite;

public class GameEntityActorState {

  public GameEntityActorState(
      ImmutableMap<Integer, NetHandleSuite> handleSuiteMap,
      ImmutableMap<Class<?>, CacheOkCommand> cmdMap,
      ActorRef dbCacheRef) {
    _handleSuiteMap = handleSuiteMap;
    _cmdMap = cmdMap;

    _dbCacheRef = dbCacheRef;
  }

  public ImmutableMap<Integer, NetHandleSuite> getHandleSuiteMap() {
    return _handleSuiteMap;
  }

  public ActorRef getDbCacheRef() {
    return _dbCacheRef;
  }

  public ImmutableMap<Class<?>, CacheOkCommand> getCmdMap() {
    return _cmdMap;
  }

  private final ImmutableMap<Integer, NetHandleSuite> _handleSuiteMap;
  private final ImmutableMap<Class<?>, CacheOkCommand> _cmdMap;

  private final ActorRef _dbCacheRef;
}
