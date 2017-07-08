package lujgame.gateway.network.akka.connection;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.Creator;
import io.netty.channel.ChannelHandlerContext;
import lujgame.core.akka.AkkaTool;
import lujgame.core.akka.schedule.ActorScheduler;
import lujgame.gateway.network.akka.accept.logic.ConnKiller;
import lujgame.gateway.network.akka.accept.logic.ForwardBinder;
import lujgame.gateway.network.akka.connection.logic.ConnInfoGetter;
import lujgame.gateway.network.akka.connection.logic.ConnPacketReceiver;
import lujgame.gateway.network.akka.connection.logic.packet.ConnPacketBuffer;
import lujgame.gateway.network.akka.connection.logic.state.ConnActorState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConnActorFactory {

  @Autowired
  public ConnActorFactory(
      AkkaTool akkaTool,
      ConnPacketReceiver connPacketReceiver,
      ForwardBinder forwardBinder,
      ConnInfoGetter connInfoGetter,
      ConnKiller connKiller) {
    _akkaTool = akkaTool;
    _connPacketReceiver = connPacketReceiver;

    _forwardBinder = forwardBinder;
    _connInfoGetter = connInfoGetter;

    _connKiller = connKiller;
  }

  public Props props(String connId, ChannelHandlerContext nettyContext, ActorRef acceptRef) {
    ConnActorState state = new ConnActorState(connId,
        new ConnPacketBuffer(), nettyContext, acceptRef);

    Creator<ConnActor> c = () -> new ConnActor(state, _akkaTool,
        _connPacketReceiver, _forwardBinder, _connKiller, _connInfoGetter);

    return Props.create(ConnActor.class, c);
  }

  public String getActorName(String connId) {
    return "Conn_" + connId;
  }

  private final AkkaTool _akkaTool;
  private final ConnPacketReceiver _connPacketReceiver;

  private final ForwardBinder _forwardBinder;
  private final ConnInfoGetter _connInfoGetter;

  private final ConnKiller _connKiller;
}
