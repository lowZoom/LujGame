package lujgame.game.server;

import akka.actor.ActorRef;
import akka.cluster.Cluster;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import lujgame.game.server.net.NetHandleSuite;

public class GameServerActorState {

  public GameServerActorState(String serverId, Cluster cluster,
      ImmutableMap<Integer, NetHandleSuite> handleSuiteMap) {
    _serverId = serverId;
    _cluster = cluster;

    _handleSuiteMap = handleSuiteMap;

    _entityMap = new HashMap<>(1024);
  }

  public String getServerId() {
    return _serverId;
  }

  public Map<String, ActorRef> getEntityMap() {
    return _entityMap;
  }

  public Cluster getCluster() {
    return _cluster;
  }

  public ImmutableMap<Integer, NetHandleSuite> getHandleSuiteMap() {
    return _handleSuiteMap;
  }

  private final Map<String, ActorRef> _entityMap;

  private final String _serverId;
  private final Cluster _cluster;

  private final ImmutableMap<Integer, NetHandleSuite> _handleSuiteMap;
}
