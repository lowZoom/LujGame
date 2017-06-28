package lujgame.game.boss;

import akka.actor.Props;
import akka.cluster.Cluster;
import akka.japi.Creator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClusterBossActorFactory {

  @Autowired
  public ClusterBossActorFactory(BossServerAdder serverAdder) {
    _serverAdder = serverAdder;
  }

  public Props props(Cluster cluster) {
    ClusterBossActorState state = new ClusterBossActorState(cluster);

    Creator<ClusterBossActor> c = () -> new ClusterBossActor(state, _serverAdder);

    return Props.create(ClusterBossActor.class, c);
  }

  private final BossServerAdder _serverAdder;
}
