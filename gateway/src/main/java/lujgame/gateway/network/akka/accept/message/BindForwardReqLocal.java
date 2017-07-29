package lujgame.gateway.network.akka.accept.message;

/**
 * 网关服内部发起，绑定投递节点请求
 */
public class BindForwardReqLocal {

  public BindForwardReqLocal(String connId, String forwardId) {
    _connId = connId;
    _forwardId = forwardId;
  }

  public String getConnId() {
    return _connId;
  }

  public String getForwardId() {
    return _forwardId;
  }

  private final String _connId;
  private final String _forwardId;
}
