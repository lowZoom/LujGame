package lujgame.core.akka.schedule;

import com.google.common.collect.Multimap;
import java.util.Map;

public class ScheduleActorState {

  public Map<String, ScheduleItem> getScheduleMap() {
    return _scheduleMap;
  }

  public void setScheduleMap(Map<String, ScheduleItem> scheduleMap) {
    _scheduleMap = scheduleMap;
  }

  public Multimap<Class<?>, ScheduleItem> getInterruptMap() {
    return _interruptMap;
  }

  public void setInterruptMap(Multimap<Class<?>, ScheduleItem> interruptMap) {
    _interruptMap = interruptMap;
  }

  private Map<String, ScheduleItem> _scheduleMap;
  private Multimap<Class<?>, ScheduleItem> _interruptMap;
}
