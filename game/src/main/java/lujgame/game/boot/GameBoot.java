package lujgame.game.boot;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.cluster.Cluster;
import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;
import lujgame.game.master.cluster.ClusterBossActorFactory;
import lujgame.game.master.gate.CommGateActorFactory;
import lujgame.game.server.GameServerActorFactory;
import lujgame.game.server.command.CacheOkCommand;
import lujgame.game.server.database.bean.DatabaseMeta;
import lujgame.game.server.net.NetHandleSuite;
import lujgame.game.server.net.NetPacketCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    Logger log = LoggerFactory.getLogger(GameBoot.class);
    log.info("游戏服开始启动...");

    GameBootConfigLoader l = _bootConfigLoader;
    String fileName = args[0];

    log.debug("读取游戏服配置：{}", fileName);
    Config gameCfg = l.loadGameConfig(fileName);
    Config akkaCfg = l.loadAkkaConfig(gameCfg);

    if (l.isSeed(gameCfg)) {
      startSeed(akkaCfg);
      return;
    }

    startGame(gameCfg, akkaCfg, log);
  }

  private void startSeed(Config akkaCfg) {
    ActorSystem system = createActorSystem(akkaCfg);
    Cluster cluster = Cluster.get(system);

    ActorRef masterRef = system.actorOf(_clusterBossActorFactory.props(cluster), "Master");
    ActorRef gateCommRef = system.actorOf(_commGateActorFactory.props(masterRef), "GateComm");
  }

  private void startGame(Config gameCfg, Config akkaCfg, Logger log) {
    log.debug("启动模式：服务节点");

    GameServerActorFactory f = _gameServerActorFactory;
    ImmutableMap<Class<?>, NetPacketCodec> packetCodecMap = f.makePacketCodecMap();
    log.debug("扫描网络包编解码器完成，数量：{}", packetCodecMap.size());

    ImmutableMap<Integer, NetHandleSuite> handleSuiteMap = f.makeHandleSuiteMap(packetCodecMap);
    log.debug("扫描网络元信息完成，数量：{}", handleSuiteMap.size());

    ImmutableMap<Class<?>, CacheOkCommand> cmdMap = f.makeCmdMap();
    log.debug("扫描逻辑处理器，数量：{}", cmdMap.size());

    ImmutableMap<Class<?>, DatabaseMeta> dbMetaMap = f.makeDatabaseMetaMap();
    log.debug("扫描数据库元信息完成，数量：{}", dbMetaMap.size());

    log.debug("启动Akka系统...");
    ActorSystem system = createActorSystem(akkaCfg);
    Cluster cluster = Cluster.get(system);

    Props props = f.props(gameCfg, cluster, handleSuiteMap, cmdMap, dbMetaMap, packetCodecMap);
    ActorRef serverRef = system.actorOf(props, "Slave");
  }

  private ActorSystem createActorSystem(Config akkaCfg) {
    return ActorSystem.create("Game", akkaCfg);
  }

  private final GameBootConfigLoader _bootConfigLoader;

  private final ClusterBossActorFactory _clusterBossActorFactory;
  private final CommGateActorFactory _commGateActorFactory;

  private final GameServerActorFactory _gameServerActorFactory;
}
