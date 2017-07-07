package lujgame.gateway.network.netty;

import akka.actor.ActorRef;
import io.netty.channel.ChannelHandlerContext;
import java.io.IOException;
import lujgame.gateway.network.akka.connection.ConnActor;
import lujgame.gateway.network.akka.connection.message.ConnDataMsg;

public class GateNettyPacket extends GateNettyData {

  public GateNettyPacket(ActorRef connRef) {
    _connRef = connRef;
  }

  @Override
  public void onDataMsg(ConnDataMsg msg) {
    _connRef.tell(msg, ActorRef.noSender());
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    if (cause.getClass() == IOException.class) {
      _connRef.tell(ConnActor.Destroy.MSG, ActorRef.noSender());
    }
  }

  private final ActorRef _connRef;
}
