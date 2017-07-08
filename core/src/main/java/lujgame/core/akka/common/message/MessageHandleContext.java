package lujgame.core.akka.common.message;

import akka.event.LoggingAdapter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import lujgame.core.akka.common.CaseActorState;

public class MessageHandleContext {

  public MessageHandleContext(CaseActorState state, Object message, List<Object> msgQueue) {
    _state = state;

    _message = message;
    _msgQueue = msgQueue;
  }

  public void addExtraMessage(Object msg) {
    _msgQueue.add(msg);
  }

  public void removeHandler(ActorMessageHandler handler) {
    Set<ActorMessageHandler> removeSet = getOrNewRemoveSet();
    removeSet.add(handler);
  }

  public LoggingAdapter log() {
    return _state.getLogger();
  }

  @SuppressWarnings("unchecked")
  public Consumer<Object> getMessageAction(Class<?> msgType) {
    Map<Class<?>, Consumer<?>> actionMap = _state.getActionMap();
    return (Consumer<Object>) actionMap.get(msgType);
  }

  public Object getMessage() {
    return _message;
  }

  @Override
  public String toString() {
    return _state.getActor().getSelf().toString();
  }

  private Set<ActorMessageHandler> getOrNewRemoveSet() {
    if (_removeSet == null) {
      _removeSet = new HashSet<>(8);
    }
    return _removeSet;
  }

  private Set<ActorMessageHandler> _removeSet;

  private final CaseActorState _state;

  private final Object _message;
  private final List<Object>  _msgQueue;
}
