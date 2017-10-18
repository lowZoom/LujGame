package lujgame.gateway.network.netty.event;

/**
 * akka请求netty发送数据时所用消息
 */
public class NettySendEvent {

  public NettySendEvent(Integer opcode, byte[] data) {
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
