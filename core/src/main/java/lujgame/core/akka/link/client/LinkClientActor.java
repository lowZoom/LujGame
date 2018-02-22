package lujgame.core.akka.link.client;

import lujgame.core.akka.common.casev2.ActorCaseHandler;
import lujgame.core.akka.common.casev2.CaseActorContext;
import lujgame.core.akka.common.casev2.CaseActorV2;
import lujgame.core.akka.common.casev2.PreStartHandler;
import lujgame.core.akka.link.server.LinkServerActor;

/**
 * 让两个远程actor保持连接
 *
 * @see LinkServerActor
 */
public class LinkClientActor extends CaseActorV2<LinkClientActorState> {

  public static class Context extends CaseActorContext<LinkClientActorState> {

  }

  public interface Case<M> extends ActorCaseHandler<Context, M> {

  }

  public interface PreStart extends PreStartHandler<Context> {

  }

  public enum TryConnect {MSG}
}
