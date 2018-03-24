package lujgame.gateway.network.akka.connection.message;

import java.io.Serializable;

public class Game2GateMsg implements Serializable {

  public Game2GateMsg(Integer opcode, byte[] data) {
    _opcode = opcode;
    _data = data;
  }

  public Integer getOpcode() {
    return _opcode;
  }

  public byte[] getData() {
    return _data;
  }

  private final Integer _opcode;

  private final byte[] _data;
}
