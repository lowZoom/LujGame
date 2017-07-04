package lujgame.gateway.glue;

import lujgame.core.akka.CaseActor;
import lujgame.gateway.network.akka.accept.logic.ForwardBinder;

public class GateGlueActor extends CaseActor {

  public GateGlueActor(
      GateGlueActorState state,
      ForwardBinder forwardBinder) {
    _state = state;

    _forwardBinder = forwardBinder;
  }

  @Override
  public void preStart() throws Exception {

  }

  private final GateGlueActorState _state;

  private final ForwardBinder _forwardBinder;
}
