package lujgame.game.gate;

import lujgame.core.akka.AkkaTool;
import lujgame.core.akka.common.CaseActor;

public class CommGateActor extends CaseActor {

  public CommGateActor(CommGateActorState state, AkkaTool akkaTool) {
    _state = state;
    _akkaTool = akkaTool;

    akkaTool.link();
  }

  private final CommGateActorState _state;

  private final AkkaTool _akkaTool;
}
