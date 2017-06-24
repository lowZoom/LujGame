package lujgame.robot.robot.instance;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import lujgame.robot.robot.spawn.logic.RobotGroup;

public class RobotInstanceState {

  public RobotInstanceState(RobotGroup robotGroup, EventLoopGroup workerGroup) {
    _robotGroup = robotGroup;
    _workerGroup = workerGroup;
  }

  public ChannelHandlerContext getNettyContext() {
    return _nettyContext;
  }

  public void setNettyContext(ChannelHandlerContext nettyContext) {
    _nettyContext = nettyContext;
  }

  public RobotGroup getRobotGroup() {
    return _robotGroup;
  }

  public EventLoopGroup getWorkerGroup() {
    return _workerGroup;
  }

  private ChannelHandlerContext _nettyContext;

  private final RobotGroup _robotGroup;
  private final EventLoopGroup _workerGroup;
}
