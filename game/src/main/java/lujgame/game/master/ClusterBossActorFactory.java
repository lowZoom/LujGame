package lujgame.game.master;

import akka.actor.Props;
import akka.cluster.Cluster;
import akka.japi.Creator;
import lujgame.core.akka.AkkaTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClusterBossActorFactory {

  @Autowired
  public ClusterBossActorFactory(
      AkkaTool akkaTool,
      GameNodeRegistrar serverRegistrar) {
    _akkaTool = akkaTool;
    _serverRegistrar = serverRegistrar;
  }

  public Props props(Cluster cluster) {
    ClusterBossActorState state = new ClusterBossActorState(cluster);

    Creator<ClusterBossActor> c = () -> new ClusterBossActor(state, _akkaTool, _serverRegistrar);

    return Props.create(ClusterBossActor.class, c);
  }

  private final AkkaTool _akkaTool;
  private final GameNodeRegistrar _serverRegistrar;
}
