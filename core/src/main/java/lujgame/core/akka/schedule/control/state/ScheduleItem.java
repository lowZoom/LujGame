package lujgame.core.akka.schedule.control.state;

import akka.actor.ActorRef;
import akka.actor.Cancellable;

public class ScheduleItem {

  public ScheduleItem(String scheduleId, Object message,
      ActorRef receiver, Cancellable cancellable) {
    _scheduleId = scheduleId;
    _message = message;

    _receiver = receiver;
    _cancellable = cancellable;
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

  private final String _scheduleId;
  private final Object _message;

  private final ActorRef _receiver;
  private final Cancellable _cancellable;
}
