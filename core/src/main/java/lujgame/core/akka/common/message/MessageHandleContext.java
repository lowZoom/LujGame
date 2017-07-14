package lujgame.core.akka.common.message;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

  public CaseActorState getActorState() {
    return _state;
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
  private final List<Object> _msgQueue;
}
