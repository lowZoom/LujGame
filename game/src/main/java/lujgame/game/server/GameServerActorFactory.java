package lujgame.game.server;

import static com.google.common.base.Preconditions.checkNotNull;

import akka.actor.Props;
import akka.cluster.Cluster;
import akka.japi.Creator;
import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;
import java.util.Map;
import lujgame.core.akka.AkkaTool;
import lujgame.core.spring.BeanCollector;
import lujgame.game.boot.GameBootConfigLoader;
import lujgame.game.master.cluster.GameNodeRegistrar;
import lujgame.game.server.database.cache.DbCacheActorFactory;
import lujgame.game.server.entity.logic.EntityBinder;
import lujgame.game.server.net.NetHandleMeta;
import lujgame.game.server.net.NetHandleSuite;
import lujgame.game.server.net.NetPacketCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameServerActorFactory {

  @Autowired
  public GameServerActorFactory(
      BeanCollector beanCollector,
      AkkaTool akkaTool,
      DbCacheActorFactory dbCacheActorFactory,
      GameBootConfigLoader bootConfigLoader,
      GameNodeRegistrar gameNodeRegistrar,
      EntityBinder entityBinder) {
    _beanCollector = beanCollector;
    _akkaTool = akkaTool;

    _dbCacheActorFactory = dbCacheActorFactory;
    _bootConfigLoader = bootConfigLoader;

    _gameNodeRegistrar = gameNodeRegistrar;
    _entityBinder = entityBinder;
  }

  public Props props(Config gameCfg, Cluster cluster) {
    String serverId = _bootConfigLoader.getServerId(gameCfg);

    ImmutableMap<Integer, NetHandleSuite> handleSuiteMap = makeHandleSuiteMap();
    GameServerActorState state = new GameServerActorState(serverId, cluster, handleSuiteMap);

    Creator<GameServerActor> c = () -> new GameServerActor(state,
        _akkaTool, _gameNodeRegistrar, _entityBinder, _dbCacheActorFactory);

    return Props.create(GameServerActor.class, c);
  }

  private ImmutableMap<Integer, NetHandleSuite> makeHandleSuiteMap() {
    BeanCollector c = _beanCollector;

    Map<Class<?>, NetPacketCodec> codecMap = c.collectBeanMap(
        NetPacketCodec.class, NetPacketCodec::packetType);

    Map<Integer, NetHandleMeta> handleMap = c.collectBeanMap(
        NetHandleMeta.class, NetHandleMeta::opcode);

    ImmutableMap.Builder<Integer, NetHandleSuite> builder = ImmutableMap.builder();
    handleMap.forEach((k, v) -> builder.put(k, makeSuite(v, getCodec(codecMap, v))));

    return builder.build();
  }

  private NetPacketCodec getCodec(Map<Class<?>, NetPacketCodec> codecMap, NetHandleMeta meta) {
    Class<?> packetType = meta.packetType();
    NetPacketCodec codec = codecMap.get(packetType);

    return checkNotNull(codec, "找不到Codec -> %s, Handler: %s",
        packetType.getName(), meta.handler().getClass().getName());
  }

  private NetHandleSuite makeSuite(NetHandleMeta meta, NetPacketCodec codec) {
    return new NetHandleSuite(meta, codec);
  }

  private final BeanCollector _beanCollector;
  private final AkkaTool _akkaTool;

  private final DbCacheActorFactory _dbCacheActorFactory;
  private final GameBootConfigLoader _bootConfigLoader;

  private final GameNodeRegistrar _gameNodeRegistrar;
  private final EntityBinder _entityBinder;
}
