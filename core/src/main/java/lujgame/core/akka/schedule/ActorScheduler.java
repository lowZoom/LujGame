package lujgame.core.akka.schedule;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Cancellable;
import akka.actor.Scheduler;
import akka.actor.UntypedActorContext;
import com.google.common.collect.Multimap;
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
  public ActorScheduler(CaseActorInternal caseActorInternal) {
    _caseActorInternal = caseActorInternal;
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

  public void schedule(CaseActor actor, long len,
      TimeUnit unit, Object msg, Class<?> interrupt) {
    UntypedActorContext ctx = actor.getContext();
    ActorSystem system = ctx.system();
    Scheduler scheduler = system.scheduler();

    FiniteDuration dur = Duration.create(len, unit);
    ActorRef actorRef = actor.getSelf();
    ExecutionContextExecutor dispatcher = system.dispatcher();

    Cancellable c = scheduler.scheduleOnce(dur, actorRef, msg, dispatcher, actorRef);
    //TODO: 要存放在某个地方，后面才能打断

    CaseActorState state = _caseActorInternal.getState(actor);
    Multimap<Class<?>, Cancellable> interruptMap = state.getScheduleInterruptMap();

  }

  private final CaseActorInternal _caseActorInternal;
}
