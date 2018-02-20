package lujgame.core.akka.schedule.message;

public class StartScheduleMsg {

  public StartScheduleMsg(long delayMs, String scheduleId, Object msg) {
    _delayMs = delayMs;

    _scheduleId = scheduleId;
    _msg = msg;
  }

  public long getDelayMs() {
    return _delayMs;
  }

  public String getScheduleId() {
    return _scheduleId;
  }

  public Object getMsg() {
    return _msg;
  }

  private final long _delayMs;

  private final String _scheduleId;

  private final Object _msg;
}
