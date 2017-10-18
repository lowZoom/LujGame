package lujgame.gateway.network.akka.connection.message;

import java.io.Serializable;

/**
 * 网关服投递出去的包裹
 */
public class Gate2GameMsg implements Serializable {

  public Gate2GameMsg(Integer opcode, byte[] data) {
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
