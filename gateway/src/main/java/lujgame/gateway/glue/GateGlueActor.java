package lujgame.gateway.glue;

import lujgame.core.akka.common.casev2.ActorCaseHandler;
import lujgame.core.akka.common.casev2.CaseActorContext;
import lujgame.core.akka.common.casev2.CaseActorV2;
import lujgame.core.akka.common.casev2.PreStartHandler;

public class GateGlueActor extends CaseActorV2<GateGlueActorState> {

  public static class Context extends CaseActorContext<GateGlueActorState> {

  }

  public interface Case<M> extends ActorCaseHandler<Context, M> {

  }

  public interface PreStart extends PreStartHandler<Context> {

  }

  public enum AdminOk {MSG}
}
