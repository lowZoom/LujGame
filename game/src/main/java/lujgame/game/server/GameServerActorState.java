package lujgame.game.server;

import akka.actor.ActorRef;
import akka.cluster.Cluster;
import java.util.HashMap;
import java.util.Map;
import lujgame.game.server.net.NetHandlerMap;

public class GameServerActorState {

  public GameServerActorState(String serverId, Cluster cluster,
      NetHandlerMap netHandlerMap) {
    _serverId = serverId;

    _cluster = cluster;
    _netHandlerMap = netHandlerMap;

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

  public NetHandlerMap getNetHandlerMap() {
    return _netHandlerMap;
  }

  private final String _serverId;
  private final Map<String, ActorRef> _entityMap;

  private final Cluster _cluster;
  private final NetHandlerMap _netHandlerMap;
}
