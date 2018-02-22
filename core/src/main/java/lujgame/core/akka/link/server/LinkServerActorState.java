package lujgame.core.akka.link.server;

import akka.actor.ActorRef;

public class LinkServerActorState {

  public ActorRef getListenerRef() {
    return _listenerRef;
  }

  public void setListenerRef(ActorRef listenerRef) {
    _listenerRef = listenerRef;
  }

  public Enum<?> getNewMsg() {
    return _newMsg;
  }

  public void setNewMsg(Enum<?> newMsg) {
    _newMsg = newMsg;
  }

  private ActorRef _listenerRef;

  private Enum<?> _newMsg;
}
