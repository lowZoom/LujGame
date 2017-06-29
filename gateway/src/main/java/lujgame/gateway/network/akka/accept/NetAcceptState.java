package lujgame.gateway.network.akka.accept;

import java.util.HashMap;
import java.util.Map;
import lujgame.gateway.network.akka.accept.logic.ConnectionItem;

public class NetAcceptState {

  public NetAcceptState() {
    _connectionMap = new HashMap<>(1024);
  }

  public Map<String, ConnectionItem> getConnectionMap() {
    return _connectionMap;
  }

  private final Map<String, ConnectionItem> _connectionMap;
}
