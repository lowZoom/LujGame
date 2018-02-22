package lujgame.core.akka.link.server;

import lujgame.core.akka.common.casev2.ActorCaseHandler;
import lujgame.core.akka.common.casev2.CaseActorContext;
import lujgame.core.akka.common.casev2.CaseActorV2;
import lujgame.core.akka.link.client.LinkClientActor;

/**
 * @see LinkClientActor
 */
public class LinkServerActor extends CaseActorV2<LinkServerActorState> {

  public static class Context extends CaseActorContext<LinkServerActorState> {

  }

  public interface Case<M> extends ActorCaseHandler<Context, M> {

  }
}
