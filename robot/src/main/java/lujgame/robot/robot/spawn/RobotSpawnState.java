package lujgame.robot.robot.spawn;

import io.netty.channel.EventLoopGroup;
import java.util.List;
import lujgame.robot.robot.config.RobotTemplate;
import lujgame.robot.robot.instance.RobotInstanceActor;

/**
 * @see RobotSpawnActor
 */
public class RobotSpawnState {

  public RobotSpawnState(List<RobotTemplate> robotTemplateList,
      List<RobotInstanceActor> robotInstanceList) {
    _robotTemplateList = robotTemplateList;
    _robotInstanceList = robotInstanceList;
  }

  public EventLoopGroup getEventLoopGroup() {
    return _eventLoopGroup;
  }

  public void setEventLoopGroup(EventLoopGroup eventLoopGroup) {
    _eventLoopGroup = eventLoopGroup;
  }

  public List<RobotTemplate> getRobotTemplateList() {
    return _robotTemplateList;
  }

  public List<RobotInstanceActor> getRobotInstanceList() {
    return _robotInstanceList;
  }

  private EventLoopGroup _eventLoopGroup;

  private final List<RobotTemplate> _robotTemplateList;

  /**
   * 已经创建的机器人实例
   */
  private final List<RobotInstanceActor> _robotInstanceList;
}
