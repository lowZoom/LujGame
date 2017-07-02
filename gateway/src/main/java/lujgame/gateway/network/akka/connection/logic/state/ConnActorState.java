package lujgame.gateway.network.akka.connection.logic.state;

import akka.actor.ActorRef;
import io.netty.channel.ChannelHandlerContext;
import lujgame.gateway.network.akka.connection.logic.packet.ConnPacketBuffer;

public class ConnActorState {

  public ConnActorState(String connId,
      ConnPacketBuffer packetBuffer,
      ChannelHandlerContext nettyContext,
      ActorRef acceptRef) {
    _connId = connId;
    _packetBuffer = packetBuffer;

    _nettyContext = nettyContext;
    _acceptRef = acceptRef;
  }

  public ActorRef getMailBox() {
    return _mailBox;
  }

  public void setMailBox(ActorRef mailBox) {
    _mailBox = mailBox;
  }

  public String getConnId() {
    return _connId;
  }

  public ConnPacketBuffer getPacketBuffer() {
    return _packetBuffer;
  }

  public ChannelHandlerContext getNettyContext() {
    return _nettyContext;
  }

  public ActorRef getAcceptRef() {
    return _acceptRef;
  }

  private ActorRef _mailBox;

  private final String _connId;

  private final ConnPacketBuffer _packetBuffer;
  private final ChannelHandlerContext _nettyContext;

  private final ActorRef _acceptRef;
}
