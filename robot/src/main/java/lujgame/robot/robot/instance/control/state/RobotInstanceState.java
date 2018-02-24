package lujgame.robot.robot.instance.control.state;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import lujgame.core.net.ReceiveBuffer;
import lujgame.robot.robot.config.RobotTemplate;

public class RobotInstanceState {

  public RobotInstanceState(RobotTemplate robotTemplate,
      EventLoopGroup workerGroup, RobotBehaveState behaveState) {
    _robotTemplate = robotTemplate;
    _workerGroup = workerGroup;

    _behaveState = behaveState;
    _receiveBuffer = new ReceiveBuffer();
  }

  public ChannelHandlerContext getNettyContext() {
    return _nettyContext;
  }

  public void setNettyContext(ChannelHandlerContext nettyContext) {
    _nettyContext = nettyContext;
  }

  public ReceiveBuffer getReceiveBuffer() {
    return _receiveBuffer;
  }

  public RobotTemplate getRobotTemplate() {
    return _robotTemplate;
  }

  public EventLoopGroup getWorkerGroup() {
    return _workerGroup;
  }

  public RobotBehaveState getBehaveState() {
    return _behaveState;
  }

  private ChannelHandlerContext _nettyContext;
  private final ReceiveBuffer _receiveBuffer;

  private final RobotTemplate _robotTemplate;
  private final EventLoopGroup _workerGroup;

  private final RobotBehaveState _behaveState;
}
