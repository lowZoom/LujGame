package lujgame.core.akka.common;

import akka.event.LoggingAdapter;
import com.google.common.collect.Multimap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Consumer;
import lujgame.core.akka.common.message.ActorMessageHandler;
import lujgame.core.akka.schedule.ScheduleItem;

public class CaseActorState {

  public CaseActorState(
      CaseActor actor,
      LoggingAdapter logger,
      LinkedList<ActorMessageHandler> messagePipeline) {
    _actor = actor;

    _logger = logger;
    _messagePipeline = messagePipeline;

    _actionMap = new HashMap<>(32);
  }

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

  public CaseActor getActor() {
    return _actor;
  }

  public Map<Class<?>, Consumer<?>> getActionMap() {
    return _actionMap;
  }

  public LoggingAdapter getLogger() {
    return _logger;
  }

  public LinkedList<ActorMessageHandler> getMessagePipeline() {
    return _messagePipeline;
  }

  private Map<String, ScheduleItem> _scheduleMap;
  private Multimap<Class<?>, ScheduleItem> _interruptMap;

  private final Map<Class<?>, Consumer<?>> _actionMap;
  private final CaseActor _actor;

  private final LoggingAdapter _logger;
  private final LinkedList<ActorMessageHandler> _messagePipeline;
}
