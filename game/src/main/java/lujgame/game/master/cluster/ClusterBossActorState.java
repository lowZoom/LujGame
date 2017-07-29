package lujgame.game.master.cluster;

import akka.actor.ActorRef;
import akka.cluster.Cluster;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClusterBossActorState {

  public ClusterBossActorState(Cluster cluster) {
    _cluster = cluster;

    _gameNodeMap = new HashMap<>(64);
    _clusterListenerSet = new HashSet<>(8);
  }

  public Cluster getCluster() {
    return _cluster;
  }

  public Map<String, ActorRef> getGameNodeMap() {
    return _gameNodeMap;
  }

  public Set<ActorRef> getClusterListenerSet() {
    return _clusterListenerSet;
  }

  private final Cluster _cluster;

  private final Map<String, ActorRef> _gameNodeMap;
  private final Set<ActorRef> _clusterListenerSet;
}
