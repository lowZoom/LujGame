package lujgame.game.server;

import akka.actor.Props;
import akka.cluster.Cluster;
import akka.japi.Creator;
import com.typesafe.config.Config;
import lujgame.core.akka.AkkaTool;
import lujgame.game.boot.GameBootConfigLoader;
import lujgame.game.master.cluster.GameNodeRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameServerActorFactory {

  @Autowired
  public GameServerActorFactory(
      GameBootConfigLoader bootConfigLoader,
      AkkaTool akkaTool,
      GameNodeRegistrar gameNodeRegistrar) {
    _bootConfigLoader = bootConfigLoader;

    _akkaTool = akkaTool;
    _gameNodeRegistrar = gameNodeRegistrar;
  }

  public Props props(Config gameCfg, Cluster cluster) {
    String serverId = _bootConfigLoader.getServerId(gameCfg);
    GameServerActorState state = new GameServerActorState(serverId, cluster);

    Creator<GameServerActor> c = () -> new GameServerActor(state, _akkaTool, _gameNodeRegistrar);
    return Props.create(GameServerActor.class, c);
  }

  private final GameBootConfigLoader _bootConfigLoader;

  private final AkkaTool _akkaTool;
  private final GameNodeRegistrar _gameNodeRegistrar;
}
