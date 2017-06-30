package lujgame.gateway.network.akka.accept;

import java.util.HashMap;
import java.util.Map;
import lujgame.gateway.network.akka.accept.logic.ConnectionItem;

public class NetAcceptState {

  public NetAcceptState() {
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

  private long _nextConnId;

  private final Map<String, ConnectionItem> _connectionMap;
}
