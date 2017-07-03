package lujgame.game.server;

import akka.cluster.Cluster;

public class GameServerActorState {

  public GameServerActorState(String serverId, Cluster cluster) {
    _serverId = serverId;
    _cluster = cluster;
  }

  public String getServerId() {
    return _serverId;
  }

  public Cluster getCluster() {
    return _cluster;
  }

  private final String _serverId;

  private final Cluster _cluster;
}
