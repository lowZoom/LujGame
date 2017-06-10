package lujgame.gateway.network.akka.accept.message;

public class KillConnMsg {

  public KillConnMsg(String connId) {
    _connId = connId;
  }

  public String getConnId() {
    return _connId;
  }

  private final String _connId;
}
