package lujgame.gateway.network.akka.accept.logic;

import akka.actor.ActorRef;

public class ConnectionItem {

  public ConnectionItem(String connId, ActorRef connRef) {
    _connId = connId;
    _connRef = connRef;
  }

  public String getConnId() {
    return _connId;
  }

  public ActorRef getConnRef() {
    return _connRef;
  }

  private final String _connId;
  private final ActorRef _connRef;
}
