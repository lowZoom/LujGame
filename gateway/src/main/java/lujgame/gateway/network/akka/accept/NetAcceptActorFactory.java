package lujgame.gateway.network.akka.accept;

import akka.actor.Props;
import akka.japi.Creator;
import lujgame.gateway.network.akka.accept.logic.NettyRunner;
import lujgame.gateway.network.akka.connection.ConnActorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NetAcceptActorFactory {

  @Autowired
  public NetAcceptActorFactory(
      NettyRunner nettyRunner,
      ConnActorFactory connActorFactory) {
    _nettyRunner = nettyRunner;
    _connActorFactory = connActorFactory;
  }

  public Props props() {
    Creator<NetAcceptActor> c = () -> new NetAcceptActor(
        new NetAcceptState(),
        _nettyRunner,
        _connActorFactory);

    return Props.create(NetAcceptActor.class, c);
  }

  private final NettyRunner _nettyRunner;
  private final ConnActorFactory _connActorFactory;
}
