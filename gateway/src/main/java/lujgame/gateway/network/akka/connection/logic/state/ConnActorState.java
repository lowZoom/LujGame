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

  public ActorRef getForwardRef() {
    return _forwardRef;
  }

  public void setForwardRef(ActorRef forwardRef) {
    _forwardRef = forwardRef;
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

  private ActorRef _forwardRef;

  private final String _connId;

  private final ConnPacketBuffer _packetBuffer;
  private final ChannelHandlerContext _nettyContext;

  private final ActorRef _acceptRef;
}
