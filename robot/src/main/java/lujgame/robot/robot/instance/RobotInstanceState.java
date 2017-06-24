package lujgame.robot.robot.instance;

import io.netty.channel.EventLoopGroup;
import lujgame.robot.robot.spawn.logic.RobotGroup;

public class RobotInstanceState {

  public RobotInstanceState(RobotGroup robotGroup, EventLoopGroup workerGroup) {
    _robotGroup = robotGroup;
    _workerGroup = workerGroup;
  }

  public RobotGroup getRobotGroup() {
    return _robotGroup;
  }

  public EventLoopGroup getWorkerGroup() {
    return _workerGroup;
  }

  private final RobotGroup _robotGroup;
  private final EventLoopGroup _workerGroup;
}
