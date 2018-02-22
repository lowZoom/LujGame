package lujgame.core.akka.schedule.control;

import akka.actor.Cancellable;
import java.util.Map;
import lujgame.core.akka.schedule.control.state.ScheduleItem;
import org.springframework.stereotype.Service;

@Service
public class ScheduleCanceller {

  public void cancel(Map<String, ScheduleItem> scheduleMap, String scheduleId) {
    ScheduleItem item = scheduleMap.get(scheduleId);
    if (item == null) {
      return;
    }
    Cancellable cancellable = item.getCancellable();
    cancellable.cancel();
  }
}
