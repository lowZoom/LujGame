package lujgame.core.akka.schedule.control.state;

import java.util.Map;

public class ScheduleActorState {

  public Map<String, ScheduleItem> getScheduleMap() {
    return _scheduleMap;
  }

  public void setScheduleMap(Map<String, ScheduleItem> scheduleMap) {
    _scheduleMap = scheduleMap;
  }

  private Map<String, ScheduleItem> _scheduleMap;
}
