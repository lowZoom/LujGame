package lujgame.core.akka.schedule;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Cancellable;
import akka.actor.Scheduler;
import akka.actor.UntypedActorContext;
import com.google.common.collect.Multimap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lujgame.core.akka.common.CaseActor;
import lujgame.core.akka.common.CaseActorInternal;
import lujgame.core.akka.common.CaseActorState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import scala.concurrent.ExecutionContextExecutor;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

@Component
public class ActorScheduler {

  @Autowired
  public ActorScheduler(CaseActorInternal caseActorInternal,
      ScheduleItemFactory scheduleItemFactory) {
    _caseActorInternal = caseActorInternal;
    _scheduleItemFactory = scheduleItemFactory;
  }

  public void schedule(CaseActor actor, long len, TimeUnit unit, Object msg) {
    UntypedActorContext ctx = actor.getContext();
    ActorSystem system = ctx.system();
    Scheduler scheduler = system.scheduler();

    FiniteDuration dur = Duration.create(len, unit);
    ActorRef actorRef = actor.getSelf();
    ExecutionContextExecutor dispatcher = system.dispatcher();

    scheduler.scheduleOnce(dur, actorRef, msg, dispatcher, actorRef);
  }

  public void scheduleSelf(CaseActor actor, long len, TimeUnit unit,
      String scheduleId, Object msg, Class<?> interruptType) {
    UntypedActorContext ctx = actor.getContext();
    ActorSystem system = ctx.system();
    Scheduler scheduler = system.scheduler();

    FiniteDuration dur = Duration.create(len, unit);
    ActorRef actorRef = actor.getSelf();
    ExecutionContextExecutor dispatcher = system.dispatcher();

    // 取消重复调度
    CaseActorInternal i = _caseActorInternal;
    CaseActorState state = i.getState(actor);
    Map<String, ScheduleItem> scheduleMap = i.getOrNewScheduleMap(state);
    cancelLast(scheduleMap, scheduleId);

    // 新建调度
    ScheduleMsg scheduleMsg = new ScheduleMsg(scheduleId);
    Cancellable c = scheduler.scheduleOnce(dur, actorRef, scheduleMsg, dispatcher, actorRef);

    // 存放新的调度
    ScheduleItem item = _scheduleItemFactory.createItem(scheduleId, msg, interruptType, c);
    scheduleMap.put(scheduleId, item);

    Multimap<Class<?>, ScheduleItem> interruptMap = i.getOrNewInterruptMap(state);
    interruptMap.put(interruptType, item);
  }

  private static void cancelLast(Map<String, ScheduleItem> scheduleMap, String scheduleId) {
    ScheduleItem item = scheduleMap.get(scheduleId);
    if (item == null) {
      return;
    }

    Cancellable cancellable = item.getCancellable();
    cancellable.cancel();
  }

  private final CaseActorInternal _caseActorInternal;
  private final ScheduleItemFactory _scheduleItemFactory;
}
