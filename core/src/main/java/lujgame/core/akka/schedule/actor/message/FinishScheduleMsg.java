package lujgame.core.akka.schedule.actor.message;

public class FinishScheduleMsg {

  public FinishScheduleMsg(String scheduleId) {
    _scheduleId = scheduleId;
  }

  public String getScheduleId() {
    return _scheduleId;
  }

  private final String _scheduleId;
}
