package lujgame.core.akka.schedule.message;

import akka.actor.ActorRef;

public class StartScheduleMsg {

  public StartScheduleMsg(String scheduleId, long delayMs, Object message, ActorRef receiver) {
    _scheduleId = scheduleId;
    _delayMs = delayMs;

    _message = message;
    _receiver = receiver;
  }

  public String getScheduleId() {
    return _scheduleId;
  }

  public long getDelayMs() {
    return _delayMs;
  }

  public Object getMessage() {
    return _message;
  }

  public ActorRef getReceiver() {
    return _receiver;
  }

  private final String _scheduleId;
  private final long _delayMs;

  private final Object _message;
  private final ActorRef _receiver;
}
