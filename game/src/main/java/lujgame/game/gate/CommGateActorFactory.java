package lujgame.game.gate;

import akka.actor.Props;
import akka.japi.Creator;
import lujgame.core.akka.AkkaTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommGateActorFactory {

  @Autowired
  public CommGateActorFactory(AkkaTool akkaTool, GameGateAdder gateAdder) {
    _akkaTool = akkaTool;
    _gateAdder = gateAdder;
  }

  public Props props() {
    CommGateActorState state = new CommGateActorState();

    Creator<CommGateActor> c = () -> {
      return new CommGateActor(state, _akkaTool, _gateAdder);
    };
    return Props.create(CommGateActor.class, c);
  }

  private final AkkaTool _akkaTool;

  private final GameGateAdder _gateAdder;
}
