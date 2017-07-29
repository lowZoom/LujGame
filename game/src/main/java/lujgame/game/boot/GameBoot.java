package lujgame.game.boot;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.cluster.Cluster;
import com.typesafe.config.Config;
import lujgame.game.master.gate.CommGateActorFactory;
import lujgame.game.master.cluster.ClusterBossActorFactory;
import lujgame.game.server.GameServerActorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameBoot {

  @Autowired
  public GameBoot(GameBootConfigLoader bootConfigLoader,
      ClusterBossActorFactory clusterBossActorFactory,
      CommGateActorFactory commGateActorFactory,
      GameServerActorFactory gameServerActorFactory) {
    _bootConfigLoader = bootConfigLoader;

    _clusterBossActorFactory = clusterBossActorFactory;
    _commGateActorFactory = commGateActorFactory;

    _gameServerActorFactory = gameServerActorFactory;
  }

  public void boot(String[] args) {
    String fileName = args[0];

    GameBootConfigLoader l = _bootConfigLoader;
    Config gameCfg = l.loadGameConfig(fileName);
    Config akkaCfg = l.loadAkkaConfig(gameCfg);

    ActorSystem system = ActorSystem.create("Game", akkaCfg);
    Cluster cluster = Cluster.get(system);

    if (l.isSeed(gameCfg)) {
      startSeed(system, cluster);
      return;
    }

    startGame(gameCfg, system, cluster);
  }

  private void startSeed(ActorSystem system, Cluster cluster) {
    ActorRef masterRef = system.actorOf(_clusterBossActorFactory.props(cluster), "Master");
    ActorRef gateCommRef = system.actorOf(_commGateActorFactory.props(masterRef), "GateComm");
  }

  private void startGame(Config gameCfg, ActorSystem system, Cluster cluster) {
    Props props = _gameServerActorFactory.props(gameCfg, cluster);
    ActorRef serverRef = system.actorOf(props, "Slave");
  }

  private final GameBootConfigLoader _bootConfigLoader;

  private final ClusterBossActorFactory _clusterBossActorFactory;
  private final CommGateActorFactory _commGateActorFactory;

  private final GameServerActorFactory _gameServerActorFactory;
}
