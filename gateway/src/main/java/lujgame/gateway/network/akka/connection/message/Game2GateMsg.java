package lujgame.gateway.network.akka.connection.message;

import java.io.Serializable;

public class Game2GateMsg implements Serializable {

  public Game2GateMsg(byte[] data) {
    _data = data;
  }

  public byte[] getData() {
    return _data;
  }

  private final byte[] _data;
}
