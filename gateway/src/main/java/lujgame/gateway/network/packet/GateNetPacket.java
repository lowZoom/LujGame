package lujgame.gateway.network.packet;

public class GateNetPacket {

  public GateNetPacket(int opcode, byte[] data) {
    _opcode = opcode;
    _data = data;
  }

  public int getOpcode() {
    return _opcode;
  }

  public byte[] getData() {
    return _data;
  }

  private final int _opcode;
  private final byte[] _data;
}
