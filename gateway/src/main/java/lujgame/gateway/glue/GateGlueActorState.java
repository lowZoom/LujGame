package lujgame.gateway.glue;

import akka.actor.ActorRef;
import java.util.HashMap;
import java.util.Map;

public class GateGlueActorState {

  public GateGlueActorState(String glueUrl) {
    _glueUrl = glueUrl;

    _forwardMap = new HashMap<>(64);
  }

  public ActorRef getAcceptRef() {
    return _acceptRef;
  }

  public void setAcceptRef(ActorRef acceptRef) {
    _acceptRef = acceptRef;
  }

  public ActorRef getAdminRef() {
    return _adminRef;
  }

  public void setAdminRef(ActorRef adminRef) {
    _adminRef = adminRef;
  }

  public Map<String, ActorRef> getForwardMap() {
    return _forwardMap;
  }

  public String getGlueUrl() {
    return _glueUrl;
  }

  /**
   * 本地连接管理节点
   */
  private ActorRef _acceptRef;

  /**
   * 远程管理节点，用于查询转发节点
   */
  private ActorRef _adminRef;

  /**
   * 投递目标节点Map：Id -> Node
   * 由管理节点主动推送，接收推送时维护
   */
  private final Map<String, ActorRef> _forwardMap;

  private final String _glueUrl;
}
