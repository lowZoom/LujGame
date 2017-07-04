package lujgame.gateway.boot;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import lujgame.gateway.glue.GateGlueActorFactory;
import lujgame.gateway.network.akka.accept.NetAcceptActorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GatewayBoot {

  @Autowired
  public GatewayBoot(
      GatewayBootConfigLoader bootConfigLoader,
      GateGlueActorFactory glueActorFactory,
      NetAcceptActorFactory netAcceptFactory) {
    _bootConfigLoader = bootConfigLoader;

    _glueActorFactory = glueActorFactory;
    _netAcceptFactory = netAcceptFactory;
  }

  public void boot(String[] args) {
    //TODO: 通用的控制台参数解析器
    String fileName = args[0];

    GatewayBootConfigLoader l = _bootConfigLoader;
    Config gateCfg = l.loadGatewayConfig(fileName);

    Config systemCfg = l.loadAkkaConfig(gateCfg);
    ActorSystem system = ActorSystem.create("LujGateway", systemCfg);

    ActorRef glueRef = system.actorOf(_glueActorFactory.props(gateCfg), "Glue");
    system.actorOf(_netAcceptFactory.props(gateCfg, glueRef), "NetAccept");
  }

  private final GatewayBootConfigLoader _bootConfigLoader;

  private final GateGlueActorFactory _glueActorFactory;
  private final NetAcceptActorFactory _netAcceptFactory;
}
