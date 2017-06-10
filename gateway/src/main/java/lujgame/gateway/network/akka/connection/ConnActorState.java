package lujgame.gateway.network.akka.connection;

import java.net.InetSocketAddress;

public class ConnActorState {

  public ConnActorState(String connId, InetSocketAddress remoteAddr) {
    _connId = connId;
    _remoteAddr = remoteAddr;
  }

  public InetSocketAddress getRemoteAddr() {
    return _remoteAddr;
  }

  private final String _connId;
  private final InetSocketAddress _remoteAddr;
}
