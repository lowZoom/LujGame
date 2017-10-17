package lujgame.gateway.network.akka.connection.message;

public class ConnSendMsg {

  public ConnSendMsg(byte[] data) {
    _data = data;
  }

  public byte[] getData() {
    return _data;
  }

  private final byte[] _data;
}
