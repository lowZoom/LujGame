package lujgame.gateway.network.akka.connection;

import java.util.function.Supplier;
import lujgame.core.akka.common.casev2.CaseActorFactory;
import lujgame.gateway.network.akka.connection.logic.state.ConnActorState;
import org.springframework.stereotype.Service;

@Service
public class GateConnActorFactory extends CaseActorFactory<
    ConnActorState,
    GateConnActor,
    GateConnActor.Context,
    GateConnActor.Case<?>> {

  @Override
  protected Class<GateConnActor> actorType() {
    return GateConnActor.class;
  }

  @Override
  protected Supplier<GateConnActor> actorConstructor() {
    return GateConnActor::new;
  }

  @Override
  protected Supplier<GateConnActor.Context> contextConstructor() {
    return GateConnActor.Context::new;
  }

  @Override
  protected Class<GateConnActor.PreStart> preStart() {
    return GateConnActor.PreStart.class;
  }
}
