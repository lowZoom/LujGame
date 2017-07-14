package lujgame.core.akka.schedule;

import akka.actor.Cancellable;
import org.springframework.stereotype.Component;

@Component
public class ScheduleItemFactory {

  public ScheduleItem createItem(String scheduleId,
      Class<?> interruptType, Cancellable cancellable) {
    return new ScheduleItem(scheduleId, interruptType, cancellable);
  }
}
