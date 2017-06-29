package lujgame.gateway.network.akka.connection.message;

public class ConnDataMsg {

  public ConnDataMsg(String connId, byte[] data) {
    _connId = connId;
    _data = data;
  }

  public String getConnId() {
    return _connId;
  }

  public byte[] getData() {
    return _data;
  }

  private final String _connId;

  private final byte[] _data;
}
