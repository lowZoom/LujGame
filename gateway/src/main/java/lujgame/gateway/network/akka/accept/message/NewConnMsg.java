package lujgame.gateway.network.akka.accept.message;

import java.net.InetSocketAddress;

public class NewConnMsg {

  public NewConnMsg(String connId, InetSocketAddress remoteAddr) {
    _connId = connId;
    _remoteAddr = remoteAddr;
  }

  public String getConnId() {
    return _connId;
  }

  public InetSocketAddress getRemoteAddr() {
    return _remoteAddr;
  }

  private final String _connId;
  private final InetSocketAddress _remoteAddr;
}
