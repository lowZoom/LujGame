package lujgame.core.akka.schedule;

import akka.actor.ActorRef;
import akka.actor.Cancellable;

public class ScheduleItem {

  public ScheduleItem(String scheduleId, Object message,
      ActorRef receiver, Cancellable cancellable, Class<?> interruptType) {
    _scheduleId = scheduleId;
    _message = message;

    _receiver = receiver;
    _cancellable = cancellable;

    _interruptType = interruptType;
  }

  public String getScheduleId() {
    return _scheduleId;
  }

  public Object getMessage() {
    return _message;
  }

  public ActorRef getReceiver() {
    return _receiver;
  }

  public Cancellable getCancellable() {
    return _cancellable;
  }

  @Deprecated
  public Class<?> getInterruptType() {
    return _interruptType;
  }

  private final String _scheduleId;
  private final Object _message;

  private final ActorRef _receiver;
  private final Cancellable _cancellable;

  private final Class<?> _interruptType;
}
