package lujgame.game.server;

import akka.actor.Props;
import akka.cluster.Cluster;
import akka.japi.Creator;
import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;
import lujgame.core.akka.AkkaTool;
import lujgame.core.spring.BeanCollector;
import lujgame.game.boot.GameBootConfigLoader;
import lujgame.game.master.cluster.GameNodeRegistrar;
import lujgame.game.server.entity.logic.EntityBinder;
import lujgame.game.server.net.GameNetHandler;
import lujgame.game.server.start.GameStarter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameServerActorFactory {

  @Autowired
  public GameServerActorFactory(
      GameBootConfigLoader bootConfigLoader,
      AkkaTool akkaTool,
      GameStarter gameStarter,
      GameNodeRegistrar gameNodeRegistrar,
      EntityBinder entityBinder) {
    _bootConfigLoader = bootConfigLoader;

    _akkaTool = akkaTool;
    _gameStarter = gameStarter;

    _gameNodeRegistrar = gameNodeRegistrar;
    _entityBinder = entityBinder;
  }

  public Props props(Config gameCfg, Cluster cluster) {
    String serverId = _bootConfigLoader.getServerId(gameCfg);
    ImmutableMap<Integer, GameNetHandler> netHandlerMap = _gameStarter.loadNetHandler();

    GameServerActorState state = new GameServerActorState(
        serverId, cluster, netHandlerMap);

    Creator<GameServerActor> c = () -> new GameServerActor(state,
        _akkaTool, _gameNodeRegistrar, _entityBinder);

    return Props.create(GameServerActor.class, c);
  }

  private final GameBootConfigLoader _bootConfigLoader;

  private final AkkaTool _akkaTool;
  private final GameStarter _gameStarter;

  private final GameNodeRegistrar _gameNodeRegistrar;
  private final EntityBinder _entityBinder;
}
