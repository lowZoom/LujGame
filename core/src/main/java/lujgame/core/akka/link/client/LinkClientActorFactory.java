package lujgame.core.akka.link.client;

import java.util.function.Supplier;
import lujgame.core.akka.common.casev2.CaseActorFactory;
import org.springframework.stereotype.Service;

@Service
public class LinkClientActorFactory extends CaseActorFactory<
    LinkClientActorState,
    LinkClientActor,
    LinkClientActor.Context,
    LinkClientActor.Case<?>> {

  @Override
  protected Class<LinkClientActor> actorType() {
    return LinkClientActor.class;
  }

  @Override
  protected Supplier<LinkClientActor> actorConstructor() {
    return LinkClientActor::new;
  }

  @Override
  protected Supplier<LinkClientActor.Context> contextConstructor() {
    return LinkClientActor.Context::new;
  }

  @Override
  protected Class<LinkClientActor.PreStart> preStart() {
    return LinkClientActor.PreStart.class;
  }
}
