package lujgame.gateway.network.akka.accept;

import akka.actor.ActorRef;
import java.util.HashMap;
import java.util.Map;
import lujgame.gateway.network.akka.accept.logic.ConnectionItem;

public class NetAcceptState {

  public NetAcceptState() {
    _nextConnId = 1;

    _connectionMap = new HashMap<>(1024);
    _forwardMap = new HashMap<>(64);
  }

  public long getNextConnId() {
    return _nextConnId;
  }

  public void setNextConnId(long nextConnId) {
    _nextConnId = nextConnId;
  }

  public ActorRef getGateBossRef() {
    return _gateBossRef;
  }

  public void setGateBossRef(ActorRef gateBossRef) {
    _gateBossRef = gateBossRef;
  }

  public Map<String, ConnectionItem> getConnectionMap() {
    return _connectionMap;
  }

  public Map<String, ActorRef> getForwardMap() {
    return _forwardMap;
  }

  private long _nextConnId;
  private ActorRef _gateBossRef;

  private final Map<String, ConnectionItem> _connectionMap;

  /**
   * 投递目标节点缓存
   */
  private final Map<String, ActorRef> _forwardMap;
}
