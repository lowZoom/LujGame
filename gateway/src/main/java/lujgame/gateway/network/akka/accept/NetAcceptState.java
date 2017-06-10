package lujgame.gateway.network.akka.accept;

import java.util.HashMap;
import java.util.Map;
import lujgame.gateway.network.akka.accept.logic.ConnItem;

public class NetAcceptState {

  public NetAcceptState() {
    _connectionMap = new HashMap<>(1024);
  }

  public Map<String, ConnItem> getConnectionMap() {
    return _connectionMap;
  }

  private final Map<String, ConnItem> _connectionMap;
}
