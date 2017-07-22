package lujgame.game.master;

import akka.actor.Props;
import akka.cluster.Cluster;
import akka.japi.Creator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClusterBossActorFactory {

  @Autowired
  public ClusterBossActorFactory(
      GameNodeRegistrar serverRegistrar,
      GateReplier gateReplier) {
    _serverRegistrar = serverRegistrar;
    _gateReplier = gateReplier;
  }

  public Props props(Cluster cluster) {
    ClusterBossActorState state = new ClusterBossActorState(cluster);

    Creator<ClusterBossActor> c = () -> new ClusterBossActor(state,
        _serverRegistrar, _gateReplier);

    return Props.create(ClusterBossActor.class, c);
  }

  private final GameNodeRegistrar _serverRegistrar;
  private final GateReplier _gateReplier;
}
