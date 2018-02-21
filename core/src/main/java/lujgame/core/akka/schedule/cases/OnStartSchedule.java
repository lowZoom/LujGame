package lujgame.core.akka.schedule.cases;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Cancellable;
import akka.actor.Scheduler;
import akka.actor.UntypedActor;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import lujgame.core.akka.internal.AkkaAdapter;
import lujgame.core.akka.schedule.ScheduleActor;
import lujgame.core.akka.schedule.ScheduleActorState;
import lujgame.core.akka.schedule.ScheduleItem;
import lujgame.core.akka.schedule.message.FinishScheduleMsg;
import lujgame.core.akka.schedule.message.StartScheduleMsg;
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
    cancelLast(scheduleMap, scheduleId);

    // 新建调度
    FinishScheduleMsg scheduleMsg = new FinishScheduleMsg(scheduleId);
    Cancellable c = _akkaAdapter.scheduleOnce(scheduler, dur,
        actor.getSelf(), scheduleMsg, dispatcher, actor.getSender());

    // 存放新的调度
    ScheduleItem item = createItem(scheduleId, msg.getMessage(), msg.getReceiver(), c);
    scheduleMap.put(scheduleId, item);

//    Multimap<Class<?>, ScheduleItem> interruptMap = getOrNewInterruptMap(state);
//    interruptMap.put(interruptType, item);
  }

  private ScheduleItem createItem(String scheduleId, Object message,
      ActorRef receiver, Cancellable cancellable) {
    return new ScheduleItem(scheduleId, message, receiver, cancellable, null);
  }

  private Map<String, ScheduleItem> getOrNewScheduleMap(ScheduleActorState state) {
    Map<String, ScheduleItem> scheduleMap = state.getScheduleMap();
    if (scheduleMap == null) {
      scheduleMap = new HashMap<>(8);
      state.setScheduleMap(scheduleMap);
    }
    return scheduleMap;
  }

  private Multimap<Class<?>, ScheduleItem> getOrNewInterruptMap(ScheduleActorState state) {
    Multimap<Class<?>, ScheduleItem> interruptMap = state.getInterruptMap();
    if (interruptMap == null) {
      interruptMap = LinkedListMultimap.create(8);
      state.setInterruptMap(interruptMap);
    }
    return interruptMap;
  }

  private static void cancelLast(Map<String, ScheduleItem> scheduleMap, String scheduleId) {
    ScheduleItem item = scheduleMap.get(scheduleId);
    if (item == null) {
      return;
    }
    Cancellable cancellable = item.getCancellable();
    cancellable.cancel();
  }

  @Inject
  private AkkaAdapter _akkaAdapter;
}
