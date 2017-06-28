package lujgame.game.boss;

import akka.cluster.Cluster;

public class ClusterBossActorState {

  public ClusterBossActorState(Cluster cluster) {
    _cluster = cluster;
  }

  public Cluster getCluster() {
    return _cluster;
  }

  private final Cluster _cluster;
}
