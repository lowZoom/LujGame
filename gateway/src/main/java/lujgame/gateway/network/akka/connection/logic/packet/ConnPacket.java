package lujgame.gateway.network.akka.connection.logic.packet;

public class ConnPacket {

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
