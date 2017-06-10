package lujgame.gateway.network.akka.connection;

import akka.actor.ActorRef;
import java.net.InetSocketAddress;

public class ConnActorState {

  public ConnActorState(String connId, InetSocketAddress remoteAddr, ActorRef acceptRef) {
    _connId = connId;
    _remoteAddr = remoteAddr;

    _acceptRef = acceptRef;
  }

  public String getConnId() {
    return _connId;
  }

  public InetSocketAddress getRemoteAddr() {
    return _remoteAddr;
  }

  public ActorRef getAcceptRef() {
    return _acceptRef;
  }

  private final String _connId;
  private final InetSocketAddress _remoteAddr;

  private final ActorRef _acceptRef;

  //TODO: 要保存一个可以和NETTY通讯的东西
}
