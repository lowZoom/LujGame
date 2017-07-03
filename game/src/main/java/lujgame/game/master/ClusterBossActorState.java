package lujgame.game.master;

import akka.actor.ActorRef;
import akka.cluster.Cluster;
import java.util.HashMap;
import java.util.Map;

public class ClusterBossActorState {

  public ClusterBossActorState(Cluster cluster) {
    _cluster = cluster;
    _gameNodeMap = new HashMap<>(64);
  }

  public Cluster getCluster() {
    return _cluster;
  }

  public Map<String, ActorRef> getGameNodeMap() {
    return _gameNodeMap;
  }

  private final Cluster _cluster;

  private final Map<String, ActorRef> _gameNodeMap;
}
