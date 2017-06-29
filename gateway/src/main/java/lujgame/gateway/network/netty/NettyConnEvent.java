package lujgame.gateway.network.netty;

import akka.actor.ActorRef;

public class NettyConnEvent {

  public NettyConnEvent(ActorRef connRef) {
    _connRef = connRef;
  }

  public ActorRef getConnRef() {
    return _connRef;
  }

  private final ActorRef _connRef;
}
