package lujgame.core.akka.schedule;

public class ScheduleMsg {

  public ScheduleMsg(Object msg, String scheduleId, Class<?> interruptType) {
    _msg = msg;

    _scheduleId = scheduleId;
    _interruptType = interruptType;
  }

  public Object getMsg() {
    return _msg;
  }

  public String getScheduleId() {
    return _scheduleId;
  }

  public Class<?> getInterruptType() {
    return _interruptType;
  }

  private final Object _msg;

  private final String _scheduleId;
  private final Class<?> _interruptType;
}
