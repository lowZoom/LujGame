package lujgame.core.akka.schedule.actor;

import lujgame.core.akka.common.casev2.ActorCaseHandler;
import lujgame.core.akka.common.casev2.CaseActorContext;
import lujgame.core.akka.common.casev2.CaseActorV2;
import lujgame.core.akka.common.casev2.PreStartHandler;
import lujgame.core.akka.schedule.control.state.ScheduleActorState;

public class ScheduleActor extends CaseActorV2<ScheduleActorState> {

  public static class Context extends CaseActorContext<ScheduleActorState> {

  }

  public interface Case<M> extends ActorCaseHandler<Context, M> {

  }

  public interface PreStart extends PreStartHandler<Context> {

  }
}
