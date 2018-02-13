package lujgame.gateway.glue;

import java.util.function.Supplier;
import lujgame.core.akka.common.casev2.CaseActorFactory;
import org.springframework.stereotype.Service;

@Service
public class GateGlueActorFactory extends CaseActorFactory<
    GateGlueActorState,
    GateGlueActor,
    GateGlueActor.Context,
    GateGlueActor.Case<?>> {

  @Override
  protected Class<GateGlueActor> actorType() {
    return GateGlueActor.class;
  }

  @Override
  protected Supplier<GateGlueActor> actorConstructor() {
    return GateGlueActor::new;
  }

  @Override
  protected Supplier<GateGlueActor.Context> contextConstructor() {
    return GateGlueActor.Context::new;
  }

  @Override
  protected Class<GateGlueActor.PreStart> preStart() {
    return GateGlueActor.PreStart.class;
  }
}
