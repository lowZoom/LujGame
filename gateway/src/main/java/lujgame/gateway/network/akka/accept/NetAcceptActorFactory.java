package lujgame.gateway.network.akka.accept;

import akka.actor.Props;
import akka.japi.Creator;
import lujgame.gateway.network.akka.accept.logic.ConnKiller;
import lujgame.gateway.network.akka.accept.logic.ForwardBinder;
import lujgame.gateway.network.akka.accept.logic.NettyRunner;
import lujgame.gateway.network.akka.accept.logic.NewConnCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NetAcceptActorFactory {

  @Autowired
  public NetAcceptActorFactory(
      NettyRunner nettyRunner,
      ForwardBinder forwardBinder,
      NewConnCreator newConnCreator,
      ConnKiller connKiller) {
    _nettyRunner = nettyRunner;
    _forwardBinder = forwardBinder;

    _newConnCreator = newConnCreator;
    _connKiller = connKiller;
  }

  public Props props() {
    Creator<NetAcceptActor> c = () -> new NetAcceptActor(
        new NetAcceptState(),
        _nettyRunner,
        _forwardBinder,
        _newConnCreator,
        _connKiller);

    return Props.create(NetAcceptActor.class, c);
  }

  private final NettyRunner _nettyRunner;
  private final ForwardBinder _forwardBinder;

  private final NewConnCreator _newConnCreator;
  private final ConnKiller _connKiller;
}
