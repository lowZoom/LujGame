package lujgame.gateway.glue;

import lujgame.core.akka.common.casev2.CaseActorFactory;
import org.springframework.stereotype.Service;

@Service
public class GateGlueActorFactory extends CaseActorFactory<
    GateGlueActorState,
    GateGlueActor,
    GateGlueActor.Context,
    GateGlueActor.Case<?>> {

  @Override
  protected GateGlueActor createActor() {
    return new GateGlueActor();
  }

  @Override
  protected GateGlueActor.Context createContext() {
    return new GateGlueActor.Context();
  }

  @Override
  protected Class<GateGlueActor.PreStart> preStart() {
    return GateGlueActor.PreStart.class;
  }
}
