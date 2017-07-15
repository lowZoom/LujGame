package lujgame.core.akka.schedule;

public class ScheduleMsg {

  public ScheduleMsg(String scheduleId) {
    _scheduleId = scheduleId;
  }

  public String getScheduleId() {
    return _scheduleId;
  }

  private final String _scheduleId;
}
