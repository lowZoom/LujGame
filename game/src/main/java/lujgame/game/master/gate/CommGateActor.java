package lujgame.game.master.gate;

import lujgame.core.akka.common.casev2.ActorCaseHandler;
import lujgame.core.akka.common.casev2.CaseActorContext;
import lujgame.core.akka.common.casev2.CaseActorV2;
import lujgame.core.akka.common.casev2.PreStartHandler;

/**
 * 负责与远程网关通讯
 */
public class CommGateActor extends CaseActorV2<CommGateActorState> {

  public static class Context extends CaseActorContext<CommGateActorState> {

  }

  public interface Case<M> extends ActorCaseHandler<Context, M> {

  }

  public interface PreStart extends PreStartHandler<Context> {

  }

  public enum NewGateConnect {MSG}
}
