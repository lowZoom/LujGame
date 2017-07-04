package lujgame.gateway.network.akka.accept;

import akka.actor.ActorRef;
import java.util.HashMap;
import java.util.Map;
import lujgame.gateway.network.akka.accept.logic.ConnectionItem;

public class NetAcceptState {

  public NetAcceptState(String host, int port, ActorRef glueRef) {
    _host = host;
    _port = port;

    _glueRef = glueRef;

    _nextConnId = 1;
    _connectionMap = new HashMap<>(1024);
  }

  public long getNextConnId() {
    return _nextConnId;
  }

  public void setNextConnId(long nextConnId) {
    _nextConnId = nextConnId;
  }

  public Map<String, ConnectionItem> getConnectionMap() {
    return _connectionMap;
  }

  public String getHost() {
    return _host;
  }

  public int getPort() {
    return _port;
  }

  public ActorRef getGlueRef() {
    return _glueRef;
  }

  private long _nextConnId;
  private final Map<String, ConnectionItem> _connectionMap;

  private final String _host;
  private final int _port;

  private final ActorRef _glueRef;
}
