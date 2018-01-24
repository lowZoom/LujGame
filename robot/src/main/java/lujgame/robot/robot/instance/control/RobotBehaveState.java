package lujgame.robot.robot.instance.control;

import io.netty.channel.ChannelHandlerContext;
import java.util.List;
import lujgame.robot.robot.config.BehaviorConfig;

public class RobotBehaveState {

  public RobotBehaveState(List<BehaviorConfig> behaviorList) {
    _behaviorList = behaviorList;
  }

  public ChannelHandlerContext getNettyContext() {
    return _nettyContext;
  }

  public void setNettyContext(ChannelHandlerContext nettyContext) {
    _nettyContext = nettyContext;
  }

  public int getBehaviorIndex() {
    return _behaviorIndex;
  }

  public void setBehaviorIndex(int behaviorIndex) {
    _behaviorIndex = behaviorIndex;
  }

  public List<BehaviorConfig> getBehaviorList() {
    return _behaviorList;
  }

  private ChannelHandlerContext _nettyContext;
  private int _behaviorIndex;

  private final List<BehaviorConfig>  _behaviorList;
}
