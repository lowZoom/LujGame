package lujgame.robot.robot.spawn;

import io.netty.channel.EventLoopGroup;
import java.util.List;
import lujgame.robot.robot.instance.RobotInstanceActor;

public class RobotSpawnState {

  public RobotSpawnState(List<RobotInstanceActor> robotList) {
    _robotList = robotList;
  }

  public EventLoopGroup getEventLoopGroup() {
    return _eventLoopGroup;
  }

  public void setEventLoopGroup(EventLoopGroup eventLoopGroup) {
    _eventLoopGroup = eventLoopGroup;
  }

  public List<RobotInstanceActor> getRobotList() {
    return _robotList;
  }

  private EventLoopGroup _eventLoopGroup;

  private final List<RobotInstanceActor> _robotList;
}
