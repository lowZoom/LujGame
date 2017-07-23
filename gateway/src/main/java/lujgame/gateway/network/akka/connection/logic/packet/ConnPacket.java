package lujgame.gateway.network.akka.connection.logic.packet;

import java.io.Serializable;

/**
 * 网关服投递出去的包裹
 */
public class ConnPacket implements Serializable {

  public ConnPacket(int opcode, byte[] data) {
    _opcode = opcode;
    _data = data;
  }

  public Integer getOpcode() {
    return _opcode;
  }

  public byte[] getData() {
    return _data;
  }

  private final int _opcode;

  private final byte[] _data;
}
