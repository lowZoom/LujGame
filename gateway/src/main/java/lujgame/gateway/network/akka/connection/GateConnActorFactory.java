package lujgame.gateway.network.akka.connection;

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
  protected GateConnActor createActor() {
    return new GateConnActor();
  }

  @Override
  protected GateConnActor.Context createContext() {
    return new GateConnActor.Context();
  }

  @Override
  protected Class<GateConnActor.PreStart> preStart() {
    return GateConnActor.PreStart.class;
  }

  @Override
  protected Class<GateConnActor.PostStop> postStop() {
    return GateConnActor.PostStop.class;
  }
}
