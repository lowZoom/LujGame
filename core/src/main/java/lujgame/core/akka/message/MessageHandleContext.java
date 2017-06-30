package lujgame.core.akka.message;

import akka.event.LoggingAdapter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import lujgame.core.akka.CaseActor;
import lujgame.core.akka.CaseActorState;

public class MessageHandleContext {

  public MessageHandleContext(
      CaseActorState state,
      Object message) {
    _state = state;
    _message = message;
  }

  public void addExtraMessage(Object msg) {
    List<Object> msgList = getOrNewExtraMessageList();
    msgList.add(msg);
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

  private List<Object> getOrNewExtraMessageList() {
    if (_extraMessageList == null) {
      _extraMessageList = new LinkedList<>();
    }
    return _extraMessageList;
  }

  private Set<ActorMessageHandler> _removeSet;
  private List<Object> _extraMessageList;

  private final Object _message;
  private final CaseActorState _state;
}
