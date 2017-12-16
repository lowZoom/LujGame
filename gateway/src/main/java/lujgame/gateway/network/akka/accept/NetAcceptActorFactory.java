package lujgame.gateway.network.akka.accept;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.Creator;
import com.typesafe.config.Config;
import lujgame.gateway.network.akka.accept.logic.ConnKiller;
import lujgame.gateway.network.akka.accept.logic.NettyRunner;
import lujgame.gateway.network.akka.accept.logic.NewConnCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NetAcceptActorFactory {

  @Autowired
  public NetAcceptActorFactory(
      NettyRunner nettyRunner,
      NewConnCreator newConnCreator,
      ConnKiller connKiller) {
    _nettyRunner = nettyRunner;

    _newConnCreator = newConnCreator;
    _connKiller = connKiller;
  }

  public Props props(Config gateConfig, ActorRef glueRef) {
    Config netCfg = gateConfig.getConfig("net-addr");

    NetAcceptState state = new NetAcceptState(
        netCfg.getString("host"), netCfg.getInt("port"), glueRef);

    Creator<NetAcceptActor> c = () -> new NetAcceptActor(
        state,
        _nettyRunner,
        _newConnCreator,
        _connKiller);

    return Props.create(NetAcceptActor.class, c);
  }

  private final NettyRunner _nettyRunner;

  private final NewConnCreator _newConnCreator;
  private final ConnKiller _connKiller;
}
