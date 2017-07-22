package lujgame.game.boot;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.cluster.Cluster;
import com.typesafe.config.Config;
import lujgame.game.gate.CommGateActorFactory;
import lujgame.game.master.ClusterBossActorFactory;
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

    if (l.isSeed(gameCfg)) {
      startSeed(gameCfg);
      return;
    }

    startGame(gameCfg);
  }

  private void startSeed(Config gameCfg) {
    Config akkaCfg = _bootConfigLoader.loadAkkaConfig(gameCfg);
    ActorSystem system = ActorSystem.create("Game", akkaCfg);

    Cluster cluster = Cluster.get(system);

    ActorRef masterRef = system.actorOf(_clusterBossActorFactory.props(cluster), "Master");
    ActorRef gateCommRef = system.actorOf(_commGateActorFactory.props(masterRef), "GateComm");
  }

  private void startGame(Config gameCfg) {
    Config akkaCfg = _bootConfigLoader.loadAkkaConfig(gameCfg);
    ActorSystem system = ActorSystem.create("Game", akkaCfg);

    Cluster cluster = Cluster.get(system);
    Props props = _gameServerActorFactory.props(gameCfg, cluster);
    ActorRef serverRef = system.actorOf(props, "Slave");
  }

  private final GameBootConfigLoader _bootConfigLoader;

  private final ClusterBossActorFactory _clusterBossActorFactory;
  private final CommGateActorFactory _commGateActorFactory;

  private final GameServerActorFactory _gameServerActorFactory;
}
