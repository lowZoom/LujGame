package lujgame.gateway.network.akka.accept.message;

import java.io.Serializable;

/**
 * 网关发给游戏服，绑定投递节点请求
 */
public class BindForwardReqRemote implements Serializable {

  public BindForwardReqRemote(String connId) {
    _connId = connId;
  }

  public String getConnId() {
    return _connId;
  }

  private final String _connId;
}
