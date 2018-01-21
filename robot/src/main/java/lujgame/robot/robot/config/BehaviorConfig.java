package lujgame.robot.robot.config;

public class BehaviorConfig {

  public BehaviorConfig(Integer opcode, String data) {
    _opcode = opcode;
    _data = data;
  }

  public Integer getOpcode() {
    return _opcode;
  }

  public String getData() {
    return _data;
  }

  private final Integer _opcode;

  private final String _data;
}
