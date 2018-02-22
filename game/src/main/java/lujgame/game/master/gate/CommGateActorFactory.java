package lujgame.game.master.gate;

import lujgame.core.akka.common.casev2.CaseActorFactory;
import org.springframework.stereotype.Service;

@Service
public class CommGateActorFactory extends CaseActorFactory<
    CommGateActorState,
    CommGateActor,
    CommGateActor.Context,
    CommGateActor.Case<?>> {

  @Override
  protected CommGateActor createActor() {
    return new CommGateActor();
  }

  @Override
  protected CommGateActor.Context createContext() {
    return new CommGateActor.Context();
  }

  @Override
  protected Class<CommGateActor.PreStart> preStart() {
    return CommGateActor.PreStart.class;
  }
}
