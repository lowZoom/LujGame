package lujgame.core.akka;

import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.Scheduler;
import org.springframework.stereotype.Service;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.FiniteDuration;

@Service
public class AkkaAdapter {

  public Cancellable scheduleOnce(Scheduler scheduler, FiniteDuration delay,
      ActorRef receiver, Object message, ExecutionContext executor, ActorRef sender) {
    return scheduler.scheduleOnce(delay, receiver, message, executor, sender);
  }
}
