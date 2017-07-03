package lujgame.game.server;

import akka.actor.Props;
import akka.cluster.Cluster;
import akka.japi.Creator;
import com.typesafe.config.Config;
import lujgame.core.akka.AkkaTool;
import lujgame.game.master.GameNodeRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameServerActorFactory {

  @Autowired
  public GameServerActorFactory(
      AkkaTool akkaTool,
      GameNodeRegistrar gameNodeRegistrar) {
    _akkaTool = akkaTool;
    _gameNodeRegistrar = gameNodeRegistrar;
  }

  public Props props(Config gameCfg, Cluster cluster) {
    String serverId = gameCfg.getString("server-id");
    GameServerActorState state = new GameServerActorState(serverId, cluster);

    Creator<GameServerActor> c = () -> new GameServerActor(state, _akkaTool, _gameNodeRegistrar);
    return Props.create(GameServerActor.class, c);
  }

  private final AkkaTool _akkaTool;
  private final GameNodeRegistrar _gameNodeRegistrar;
}
