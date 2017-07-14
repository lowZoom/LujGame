package lujgame.core.akka.common;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import java.util.HashMap;
import java.util.Map;
import lujgame.core.akka.schedule.ScheduleItem;
import org.springframework.stereotype.Component;

@Component
public class CaseActorInternal {

  public CaseActorState getState(CaseActor actor) {
    return actor._state;
  }

  public Map<String, ScheduleItem> getOrNewScheduleMap(CaseActorState state) {
    Map<String, ScheduleItem> scheduleMap = state.getScheduleMap();
    if (scheduleMap == null) {
      scheduleMap = new HashMap<>(8);
      state.setScheduleMap(scheduleMap);
    }
    return scheduleMap;
  }

  public Multimap<Class<?>, ScheduleItem> getOrNewInterruptMap(CaseActorState state) {
    Multimap<Class<?>, ScheduleItem> interruptMap = state.getInterruptMap();
    if (interruptMap == null) {
      interruptMap = LinkedListMultimap.create(8);
      state.setInterruptMap(interruptMap);
    }
    return interruptMap;
  }
}
