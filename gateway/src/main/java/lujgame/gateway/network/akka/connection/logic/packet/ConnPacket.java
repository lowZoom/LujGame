package lujgame.gateway.network.akka.connection.logic.packet;

public class ConnPacket {

  public Integer getOpcode() {
    return _opcode;
  }

  public void setOpcode(Integer opcode) {
    _opcode = opcode;
  }

  private Integer _opcode;
}
