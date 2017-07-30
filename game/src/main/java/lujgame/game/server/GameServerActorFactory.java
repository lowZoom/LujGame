package lujgame.game.server;

import akka.actor.Props;
import akka.cluster.Cluster;
import akka.japi.Creator;
import com.typesafe.config.Config;
import lujgame.core.akka.AkkaTool;
import lujgame.core.spring.BeanCollector;
import lujgame.game.boot.GameBootConfigLoader;
import lujgame.game.master.cluster.GameNodeRegistrar;
import lujgame.game.server.entity.logic.EntityBinder;
import lujgame.game.server.net.NetHandlerMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameServerActorFactory {

  @Autowired
  public GameServerActorFactory(
      NetHandlerMap netHandlerMap,
      GameBootConfigLoader bootConfigLoader,
      AkkaTool akkaTool,
      GameNodeRegistrar gameNodeRegistrar,
      EntityBinder entityBinder) {
    _netHandlerMap = netHandlerMap;
    _bootConfigLoader = bootConfigLoader;

    _akkaTool = akkaTool;
    _gameNodeRegistrar = gameNodeRegistrar;

    _entityBinder = entityBinder;
  }

  public Props props(Config gameCfg, Cluster cluster) {
    String serverId = _bootConfigLoader.getServerId(gameCfg);

    GameServerActorState state = new GameServerActorState(
        serverId, cluster, _netHandlerMap);

    Creator<GameServerActor> c = () -> new GameServerActor(state,
        _akkaTool, _gameNodeRegistrar, _entityBinder);

    return Props.create(GameServerActor.class, c);
  }

  private final NetHandlerMap _netHandlerMap;
  private final GameBootConfigLoader _bootConfigLoader;

  private final AkkaTool _akkaTool;
  private final GameNodeRegistrar _gameNodeRegistrar;

  private final EntityBinder _entityBinder;
}
