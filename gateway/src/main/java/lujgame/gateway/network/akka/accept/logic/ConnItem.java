package lujgame.gateway.network.akka.accept.logic;

import akka.actor.ActorRef;

public class ConnItem {

  public ConnItem(String connId, ActorRef connRef) {
    _connId = connId;
    _connRef = connRef;
  }

  public String getConnId() {
    return _connId;
  }

  private final String _connId;
  private final ActorRef _connRef;
}
