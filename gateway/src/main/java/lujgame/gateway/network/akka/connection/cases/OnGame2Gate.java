package lujgame.gateway.network.akka.connection.cases;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import javax.inject.Inject;
import lujgame.gateway.network.akka.connection.GateConnActor;
import lujgame.gateway.network.akka.connection.logic.ConnPacketSender;
import lujgame.gateway.network.akka.connection.logic.state.ConnActorState;
import lujgame.gateway.network.akka.connection.message.Game2GateMsg;
import org.springframework.stereotype.Service;

@Service
class OnGame2Gate implements GateConnActor.Case<Game2GateMsg> {

  @Override
  public void onHandle(GateConnActor.Context ctx) {
    ConnActorState state = ctx.getActorState();
    Game2GateMsg msg = ctx.getMessage(this);

    ActorRef connRef = ctx.getActor().getSelf();
    LoggingAdapter log = ctx.getActorLogger();

    _packetSender.sendPacket(state.getNettyContext(), 233, msg.getData());
  }

  @Inject
  private ConnPacketSender _packetSender;
}
