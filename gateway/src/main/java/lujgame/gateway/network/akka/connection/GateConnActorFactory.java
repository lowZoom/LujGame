package lujgame.gateway.network.akka.connection;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.Creator;
import io.netty.channel.ChannelHandlerContext;
import lujgame.gateway.network.akka.accept.logic.ConnKiller;
import lujgame.gateway.network.akka.accept.logic.ForwardBinder;
import lujgame.gateway.network.akka.connection.logic.ConnInfoGetter;
import lujgame.gateway.network.akka.connection.logic.ConnPacketReceiver;
import lujgame.gateway.network.akka.connection.logic.DumbDetector;
import lujgame.gateway.network.akka.connection.logic.packet.ConnPacketBuffer;
import lujgame.gateway.network.akka.connection.logic.state.ConnActorState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GateConnActorFactory {

  @Autowired
  public GateConnActorFactory(
      ConnPacketReceiver connPacketReceiver,
      ForwardBinder forwardBinder,
      ConnInfoGetter connInfoGetter,
      ConnKiller connKiller,
      DumbDetector dumbDetector) {
    _connPacketReceiver = connPacketReceiver;

    _forwardBinder = forwardBinder;
    _connInfoGetter = connInfoGetter;

    _connKiller = connKiller;
    _dumbDetector = dumbDetector;
  }

  public Props props(String connId, ChannelHandlerContext nettyContext, ActorRef acceptRef) {
    ConnActorState state = new ConnActorState(connId,
        new ConnPacketBuffer(), nettyContext, acceptRef);

    Creator<GateConnActor> c = () -> new GateConnActor(state, _connPacketReceiver,
        _forwardBinder, _connKiller, _dumbDetector, _connInfoGetter);

    return Props.create(GateConnActor.class, c);
  }

  public String getActorName(String connId) {
    return "Conn_" + connId;
  }

  private final ConnPacketReceiver _connPacketReceiver;

  private final ForwardBinder _forwardBinder;
  private final ConnKiller _connKiller;

  private final DumbDetector _dumbDetector;
  private final ConnInfoGetter _connInfoGetter;
}
