package lujgame.core.akka.schedule;

import akka.actor.Cancellable;

public class ScheduleItem {

  public ScheduleItem(String scheduleId, Class<?> interruptType, Cancellable cancellable) {
    _scheduleId = scheduleId;

    _interruptType = interruptType;
    _cancellable = cancellable;
  }

  public String getScheduleId() {
    return _scheduleId;
  }

  public Class<?> getInterruptType() {
    return _interruptType;
  }

  public Cancellable getCancellable() {
    return _cancellable;
  }

  private final String _scheduleId;

  private final Class<?> _interruptType;
  private final Cancellable _cancellable;
}
