package lujgame.game.master.cluster.message;

import akka.actor.ActorRef;

public class ReplyGateMsg {

  public ReplyGateMsg(ActorRef gateRef) {
    _gateRef = gateRef;
  }

  public ActorRef getGateRef() {
    return _gateRef;
  }

  private final ActorRef _gateRef;
}
