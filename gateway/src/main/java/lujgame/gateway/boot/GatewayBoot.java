package lujgame.gateway.boot;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import lujgame.core.file.DataFileReader;
import lujgame.gateway.network.akka.accept.NetAcceptActorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GatewayBoot {

  @Autowired
  public GatewayBoot(
      DataFileReader dataFileReader,
      NetAcceptActorFactory netAcceptFactory) {
    _dataFileReader = dataFileReader;
    _netAcceptFactory = netAcceptFactory;
  }

  public void boot() {
    Config systemCfg = _dataFileReader.readConfig("akka.conf");
    ActorSystem system = ActorSystem.create("Gateway", systemCfg);

    Props props = _netAcceptFactory.props();
    system.actorOf(props);
  }

  private final DataFileReader _dataFileReader;
  private final NetAcceptActorFactory _netAcceptFactory;
}
