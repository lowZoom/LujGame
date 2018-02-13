package lujgame.gateway.network.akka.connection.cases;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import javax.inject.Inject;
import lujgame.gateway.network.akka.connection.GateConnActor;
import lujgame.gateway.network.akka.connection.logic.DumbDetector;
import lujgame.gateway.network.akka.connection.logic.state.ConnActorState;
import org.springframework.stereotype.Service;

@Service
class OnDumb implements GateConnActor.Case<GateConnActor.Dumb> {

  @Override
  public void onHandle(GateConnActor.Context ctx) {
    ConnActorState state = ctx.getActorState();

    ActorRef connRef = ctx.getActor().getSelf();
    LoggingAdapter log = ctx.getActorLogger();

    _dumbDetector.destroyDumb(state, connRef, log);
  }

  @Inject
  private DumbDetector _dumbDetector;
}
