package lujgame.gateway.network.akka.connection.logic.packet;

public class ConnPacketHeader {

  public static final int HEADER_SIZE = 6;

  public ConnPacketHeader(int opcode, int length) {
    _opcode = opcode;
    _length = length;
  }

  public int getOpcode() {
    return _opcode;
  }

  public int getLength() {
    return _length;
  }

  private final int _opcode;

  private final int _length;
}
