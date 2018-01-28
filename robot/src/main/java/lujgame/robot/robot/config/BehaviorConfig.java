package lujgame.robot.robot.config;

public class BehaviorConfig {

  public enum Wait {
    TIME,
    RESPONSE,
  }

  public BehaviorConfig(Integer opcode, String data, Wait waitOption, long waitMilli) {
    _opcode = opcode;
    _data = data;

    _waitOption = waitOption;
    _waitMilli = waitMilli;
  }

  public Integer getOpcode() {
    return _opcode;
  }

  public String getData() {
    return _data;
  }

  public Wait getWaitOption() {
    return _waitOption;
  }

  public long getWaitMilli() {
    return _waitMilli;
  }

  private final Integer _opcode;
  private final String _data;

  private final Wait _waitOption;
  private final long _waitMilli;
}
