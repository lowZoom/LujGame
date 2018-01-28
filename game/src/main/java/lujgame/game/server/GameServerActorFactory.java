package lujgame.game.server;

import static com.google.common.base.Preconditions.checkNotNull;

import akka.actor.Props;
import akka.cluster.Cluster;
import akka.japi.Creator;
import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;
import java.util.Map;
import lujgame.core.akka.AkkaTool;
import lujgame.core.spring.SpringBeanCollector;
import lujgame.game.boot.GameBootConfigLoader;
import lujgame.game.master.cluster.GameNodeRegistrar;
import lujgame.game.server.command.CacheOkCommand;
import lujgame.game.server.database.bean.DatabaseMeta;
import lujgame.game.server.database.cache.DbCacheActorFactory;
import lujgame.game.server.entity.internal.EntityBinder;
import lujgame.game.server.net.NetHandleMeta;
import lujgame.game.server.net.NetHandleSuite;
import lujgame.game.server.net.packet.NetPacketCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameServerActorFactory {

  @Autowired
  public GameServerActorFactory(
      AkkaTool akkaTool,
      SpringBeanCollector beanCollector,
      GameBootConfigLoader bootConfigLoader,
      GameNodeRegistrar gameNodeRegistrar,
      EntityBinder entityBinder,
      DbCacheActorFactory dbCacheActorFactory) {
    _akkaTool = akkaTool;
    _beanCollector = beanCollector;

    _bootConfigLoader = bootConfigLoader;
    _gameNodeRegistrar = gameNodeRegistrar;

    _entityBinder = entityBinder;
    _dbCacheActorFactory = dbCacheActorFactory;
  }

  public Props props(Config gameCfg, Cluster cluster,
      ImmutableMap<Integer, NetHandleSuite> handleSuiteMap,
      ImmutableMap<Class<?>, CacheOkCommand> cmdMap,
      ImmutableMap<Class<?>, DatabaseMeta> databaseMetaMap,
      ImmutableMap<Class<?>, NetPacketCodec> netPacketCodecMap) {
    String serverId = _bootConfigLoader.getServerId(gameCfg);

    GameServerActorState state = new GameServerActorState(serverId,
        gameCfg, cluster, handleSuiteMap, cmdMap, databaseMetaMap, netPacketCodecMap);

    Creator<GameServerActor> c = () -> new GameServerActor(state,
        _akkaTool, _gameNodeRegistrar, _entityBinder, _dbCacheActorFactory);

    return Props.create(GameServerActor.class, c);
  }

  public ImmutableMap<Class<?>, NetPacketCodec> makePacketCodecMap() {
    return _beanCollector.collectBeanMap(NetPacketCodec.class, NetPacketCodec::packetType);
  }

  public ImmutableMap<Integer, NetHandleSuite> makeHandleSuiteMap(
      Map<Class<?>, NetPacketCodec> codecMap) {
    Map<Integer, NetHandleMeta> handleMap = _beanCollector
        .collectBeanMap(NetHandleMeta.class, NetHandleMeta::opcode);

    ImmutableMap.Builder<Integer, NetHandleSuite> builder = ImmutableMap.builder();
    handleMap.forEach((k, v) -> builder.put(k, makeSuite(v, codecMap)));

    return builder.build();
  }

  public ImmutableMap<Class<?>, CacheOkCommand> makeCmdMap() {
    return _beanCollector.collectBeanMap(CacheOkCommand.class, Object::getClass);
  }

  public ImmutableMap<Class<?>, DatabaseMeta> makeDatabaseMetaMap() {
    return _beanCollector.collectBeanMap(DatabaseMeta.class, DatabaseMeta::databaseType);
  }

  private NetHandleSuite makeSuite(NetHandleMeta meta, Map<Class<?>, NetPacketCodec> codecMap) {
    Class<?> packetType = meta.packetType();
    NetPacketCodec codec = codecMap.get(packetType);

    checkNotNull(codec, "找不到Codec -> %s, Handler: %s",
        packetType.getName(), meta.handler().getClass().getName());

    return new NetHandleSuite(meta, codec);
  }

  private final AkkaTool _akkaTool;
  private final SpringBeanCollector _beanCollector;

  private final GameBootConfigLoader _bootConfigLoader;
  private final GameNodeRegistrar _gameNodeRegistrar;

  private final EntityBinder _entityBinder;
  private final DbCacheActorFactory _dbCacheActorFactory;
}
