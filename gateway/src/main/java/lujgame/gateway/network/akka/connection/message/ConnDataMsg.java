package lujgame.gateway.network.akka.connection.message;

public class ConnDataMsg {

  public ConnDataMsg(byte[] data) {
    _data = data;
  }

  public byte[] getData() {
    return _data;
  }

  private final byte[] _data;
}
