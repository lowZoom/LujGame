package lujgame.core.akka.schedule.actor.cases;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import javax.inject.Inject;
import lujgame.core.akka.schedule.actor.ScheduleActor;
import lujgame.core.akka.schedule.actor.message.CancelScheduleMsg;
import lujgame.core.akka.schedule.control.ScheduleCanceller;
import lujgame.core.akka.schedule.control.state.ScheduleActorState;
import lujgame.core.akka.schedule.control.state.ScheduleItem;
import org.springframework.stereotype.Service;

/**
 * 中途取消定时任务
 */
@Service
public class OnCancelSchedule implements ScheduleActor.Case<CancelScheduleMsg> {

  @Override
  public void onHandle(ScheduleActor.Context ctx) {
    ScheduleActorState actorState = ctx.getActorState();
    CancelScheduleMsg msg = ctx.getMessage(this);

    Map<String, ScheduleItem> scheduleMap = actorState.getScheduleMap();
    checkNotNull(scheduleMap);

    String scheduleId = msg.getScheduleId();
    _scheduleCanceller.cancel(scheduleMap, scheduleId);
  }

  @Inject
  private ScheduleCanceller _scheduleCanceller;
}
