package lujgame.robot.robot.instance.message;

import io.netty.buffer.ByteBuf;

public class Netty2RobotMsg {

  public Netty2RobotMsg(ByteBuf dataBuf) {
    _dataBuf = dataBuf;
  }

  public ByteBuf getDataBuf() {
    return _dataBuf;
  }

  private final ByteBuf _dataBuf;
}
