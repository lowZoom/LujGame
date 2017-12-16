package lujgame.gateway.network.akka.connection;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.Creator;
import io.netty.channel.ChannelHandlerContext;
import javax.inject.Inject;
import lujgame.gateway.network.akka.connection.logic.GateConnActorDependency;
import lujgame.gateway.network.akka.connection.logic.packet.ConnPacketBuffer;
import lujgame.gateway.network.akka.connection.logic.state.ConnActorState;
import org.springframework.stereotype.Service;

@Service
public class GateConnActorFactory {

  public Props props(String connId, ChannelHandlerContext nettyContext, ActorRef acceptRef) {
    ConnActorState state = new ConnActorState(connId,
        new ConnPacketBuffer(), nettyContext, acceptRef);

    Creator<GateConnActor> c = () -> new GateConnActor(state, _gateConnActorDependency);
    return Props.create(GateConnActor.class, c);
  }

  public String getActorName(String connId) {
    return "Conn_" + connId;
  }

  @Inject
  private GateConnActorDependency _gateConnActorDependency;
}
