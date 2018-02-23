package lujgame.core.akka.schedule.actor.cases;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Cancellable;
import akka.actor.Scheduler;
import akka.actor.UntypedActor;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import lujgame.core.akka.AkkaAdapter;
import lujgame.core.akka.schedule.actor.ScheduleActor;
import lujgame.core.akka.schedule.actor.message.FinishScheduleMsg;
import lujgame.core.akka.schedule.actor.message.StartScheduleMsg;
import lujgame.core.akka.schedule.control.ScheduleCanceller;
import lujgame.core.akka.schedule.control.state.ScheduleActorState;
import lujgame.core.akka.schedule.control.state.ScheduleItem;
import org.springframework.stereotype.Service;
import scala.concurrent.ExecutionContextExecutor;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

@Service
public class OnStartSchedule implements ScheduleActor.Case<StartScheduleMsg> {

  @Override
  public void onHandle(ScheduleActor.Context ctx) {
    UntypedActor actor = ctx.getActor();
    ActorSystem system = actor.getContext().system();
    Scheduler scheduler = system.scheduler();

    StartScheduleMsg msg = ctx.getMessage(this);
    FiniteDuration dur = Duration.create(msg.getDelayMs(), TimeUnit.MILLISECONDS);
    ExecutionContextExecutor dispatcher = system.dispatcher();

    ScheduleActorState state = ctx.getActorState();
    String scheduleId = msg.getScheduleId();

    // 取消重复调度
    Map<String, ScheduleItem> scheduleMap = getOrNewScheduleMap(state);
    _scheduleCanceller.cancel(scheduleMap, scheduleId);

    // 新建调度
    FinishScheduleMsg scheduleMsg = new FinishScheduleMsg(scheduleId);
    Cancellable c = _akkaAdapter.scheduleOnce(scheduler, dur,
        actor.getSelf(), scheduleMsg, dispatcher, actor.getSender());

    // 存放新的调度
    ScheduleItem item = createItem(scheduleId, msg.getMessage(), msg.getReceiver(), c);
    scheduleMap.put(scheduleId, item);
  }

  private ScheduleItem createItem(String scheduleId, Object message,
      ActorRef receiver, Cancellable cancellable) {
    return new ScheduleItem(scheduleId, message, receiver, cancellable);
  }

  private Map<String, ScheduleItem> getOrNewScheduleMap(ScheduleActorState state) {
    Map<String, ScheduleItem> scheduleMap = state.getScheduleMap();
    if (scheduleMap == null) {
      scheduleMap = new HashMap<>(8);
      state.setScheduleMap(scheduleMap);
    }
    return scheduleMap;
  }

  @Inject
  private AkkaAdapter _akkaAdapter;

  @Inject
  private ScheduleCanceller _scheduleCanceller;
}
