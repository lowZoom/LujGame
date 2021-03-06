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
public class OnGame2Gate implements GateConnActor.Case<Game2GateMsg> {

  @Override
  public void onHandle(GateConnActor.Context ctx) {
    ConnActorState state = ctx.getActorState();
    Game2GateMsg msg = ctx.getMessage(this);

    ActorRef connRef = ctx.getActor().getSelf();
    LoggingAdapter log = ctx.getActorLogger();

    Integer opcode = msg.getOpcode();
    log.debug("网关回包：{}", opcode);

    _packetSender.sendPacket(state.getNettyContext(), opcode, msg.getData());
  }

  @Inject
  private ConnPacketSender _packetSender;
}
