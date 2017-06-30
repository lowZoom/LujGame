package lujgame.gateway.network.netty;

import akka.actor.ActorRef;
import lujgame.gateway.network.akka.connection.message.ConnDataMsg;

public class GateNettyPacket extends GateNettyData {

  public GateNettyPacket(ActorRef connRef) {
    _connRef = connRef;
  }

  @Override
  public void onDataMsg(ConnDataMsg msg) {
    _connRef.tell(msg, ActorRef.noSender());
  }

  private final ActorRef _connRef;
}
