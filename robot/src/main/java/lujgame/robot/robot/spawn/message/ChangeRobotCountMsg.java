package lujgame.robot.robot.spawn.message;

import java.io.Serializable;

/**
 * 改变机器人数量的触发消息
 */
public class ChangeRobotCountMsg implements Serializable {

  public ChangeRobotCountMsg(int count) {
    _count = count;
  }

  public int getCount() {
    return _count;
  }

  private final int _count;
}
