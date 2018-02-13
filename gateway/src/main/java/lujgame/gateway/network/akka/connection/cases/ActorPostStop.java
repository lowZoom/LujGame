package lujgame.gateway.network.akka.connection.cases;

import lujgame.gateway.network.akka.connection.GateConnActor;
import lujgame.gateway.network.akka.connection.logic.state.ConnActorState;
import org.springframework.stereotype.Service;

@Service
class ActorPostStop implements GateConnActor.PostStop {

  @Override
  public void postStop(GateConnActor.Context ctx) throws Exception {
    ConnActorState state = ctx.getActorState();

//    log().debug("连接被销毁 -> {}", _state.getConnId());
    state.getNettyContext().close();
  }
}
