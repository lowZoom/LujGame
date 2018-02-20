package lujgame.core.akka.schedule;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Scheduler;
import akka.actor.UntypedActorContext;
import java.util.concurrent.TimeUnit;
import lujgame.core.akka.common.CaseActor;
import lujgame.core.akka.feature.ActorFeature;
import lujgame.core.akka.feature.FeatureDispatchMsg;
import lujgame.core.akka.internal.AkkaAdapter;
import lujgame.core.akka.schedule.message.StartScheduleMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.concurrent.ExecutionContextExecutor;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

@Service
public class ActorScheduler {

  public void schedule(CaseActor actor, long len, TimeUnit unit, Object msg) {
    UntypedActorContext ctx = actor.getContext();
    ActorSystem system = ctx.system();
    Scheduler scheduler = system.scheduler();

    FiniteDuration dur = Duration.create(len, unit);
    ActorRef actorRef = actor.getSelf();
    ExecutionContextExecutor dispatcher = system.dispatcher();

    _akkaAdapter.scheduleOnce(scheduler, dur, actorRef, msg, dispatcher, actorRef);
  }

  public void scheduleSelf(ActorRef actorRef, long delayMs, String scheduleId, Object msg) {
    StartScheduleMsg scheduleMsg = new StartScheduleMsg(delayMs, scheduleId, msg);

    FeatureDispatchMsg featureDispatchMsg = new FeatureDispatchMsg(
        ActorFeature.SCHEDULE, scheduleMsg);

    actorRef.tell(featureDispatchMsg, actorRef);
  }

  @Autowired
  private AkkaAdapter _akkaAdapter;
}
