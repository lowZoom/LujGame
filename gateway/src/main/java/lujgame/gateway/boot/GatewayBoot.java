package lujgame.gateway.boot;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import lujgame.core.file.DataFileReader;
import lujgame.gateway.network.akka.accept.NetAcceptActor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GatewayBoot {

  @Autowired
  public GatewayBoot(
      DataFileReader dataFileReader) {
    _dataFileReader = dataFileReader;
  }

  public void boot() {
    Config systemCfg = _dataFileReader.readConfig("akka.conf");
    ActorSystem system = ActorSystem.create("Gateway", systemCfg);

    system.actorOf(Props.create(NetAcceptActor.class, NetAcceptActor::new));
  }

  private final DataFileReader _dataFileReader;
}
