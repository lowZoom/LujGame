package lujgame.robot.netty;

public class RobotNetPacket {

  public RobotNetPacket(int opcode, byte[] data) {
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
