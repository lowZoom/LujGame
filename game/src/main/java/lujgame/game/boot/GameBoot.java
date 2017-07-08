package lujgame.game.boot;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.cluster.Cluster;
import com.typesafe.config.Config;
import lujgame.game.master.ClusterBossActorFactory;
import lujgame.game.server.GameServerActorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameBoot {

  @Autowired
  public GameBoot(GameBootConfigLoader bootConfigLoader,
      ClusterBossActorFactory clusterBossActorFactory,
      GameServerActorFactory gameServerActorFactory) {
    _bootConfigLoader = bootConfigLoader;

    _clusterBossActorFactory = clusterBossActorFactory;
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
    Props props = _clusterBossActorFactory.props(cluster);

    ActorRef bossRef = system.actorOf(props, "Master");

    //TODO: 启动网关管理节点
  }

  private void startGame(Config gameCfg) {
    GameBootConfigLoader l = _bootConfigLoader;
    Config akkaCfg = l.loadAkkaConfig(gameCfg);
    ActorSystem system = ActorSystem.create("Game", akkaCfg);

    Cluster cluster = Cluster.get(system);
    Props props = _gameServerActorFactory.props(gameCfg, cluster);
    ActorRef serverRef = system.actorOf(props);
  }

  private final GameBootConfigLoader _bootConfigLoader;

  private final ClusterBossActorFactory _clusterBossActorFactory;
  private final GameServerActorFactory _gameServerActorFactory;
}
