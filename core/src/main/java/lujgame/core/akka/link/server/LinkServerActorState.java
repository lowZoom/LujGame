package lujgame.core.akka.link.server;

import akka.actor.ActorRef;

public class LinkServerActorState {

  public LinkServerActorState(ActorRef listenerRef, Enum<?> newMsg) {
    _listenerRef = listenerRef;
    _newMsg = newMsg;
  }

  public ActorRef getListenerRef() {
    return _listenerRef;
  }

  public Enum<?> getNewMsg() {
    return _newMsg;
  }

  private final ActorRef _listenerRef;

  private final Enum<?> _newMsg;
}
