package lujgame.gateway.network.akka.connection;

import akka.actor.ActorRef;
import io.netty.channel.ChannelHandlerContext;

public class ConnActorState {

  public ConnActorState(String connId, ChannelHandlerContext nettyContext, ActorRef acceptRef) {
    _connId = connId;

    _nettyContext = nettyContext;
    _acceptRef = acceptRef;
  }

  public String getConnId() {
    return _connId;
  }

  public ChannelHandlerContext getNettyContext() {
    return _nettyContext;
  }

  public ActorRef getAcceptRef() {
    return _acceptRef;
  }

  private final String _connId;

  private final ChannelHandlerContext _nettyContext;
  private final ActorRef _acceptRef;
}
