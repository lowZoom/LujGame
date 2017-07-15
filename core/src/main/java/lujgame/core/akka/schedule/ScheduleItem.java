package lujgame.core.akka.schedule;

import akka.actor.Cancellable;

public class ScheduleItem {

  public ScheduleItem(String scheduleId,
      Object message,
      Class<?> interruptType,
      Cancellable cancellable) {
    _scheduleId = scheduleId;
    _message = message;

    _interruptType = interruptType;
    _cancellable = cancellable;
  }

  public String getScheduleId() {
    return _scheduleId;
  }

  public Object getMessage() {
    return _message;
  }

  public Cancellable getCancellable() {
    return _cancellable;
  }

  public Class<?> getInterruptType() {
    return _interruptType;
  }

  private final String _scheduleId;
  private final Object _message;

  private final Class<?> _interruptType;
  private final Cancellable _cancellable;
}
