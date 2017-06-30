package lujgame.core.akka;

import akka.event.LoggingAdapter;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Consumer;
import lujgame.core.akka.message.ActorMessageHandler;

public class CaseActorState {

  public CaseActorState(
      CaseActor actor,
      Map<Class<?>, Consumer<?>> actionMap,
      LoggingAdapter logger,
      LinkedList<ActorMessageHandler> messagePipeline,
      LinkedList<Object> messageQueue) {
    _actor = actor;

    _actionMap = actionMap;
    _logger = logger;

    _messagePipeline = messagePipeline;
    _messageQueue = messageQueue;
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

  public LinkedList<Object> getMessageQueue() {
    return _messageQueue;
  }

  private final CaseActor _actor;

  private final Map<Class<?>, Consumer<?>> _actionMap;
  private final LoggingAdapter _logger;

  private final LinkedList<ActorMessageHandler> _messagePipeline;
  private final LinkedList<Object> _messageQueue;
}
