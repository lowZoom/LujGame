package lujgame.gateway.network.akka.accept.message;

import java.io.Serializable;

public class BindForwardReq implements Serializable {

  public BindForwardReq(String connId, String boxId) {
    _connId = connId;
    _boxId = boxId;
  }

  public String getConnId() {
    return _connId;
  }

  public String getBoxId() {
    return _boxId;
  }

  private final String _connId;
  private final String _boxId;
}
