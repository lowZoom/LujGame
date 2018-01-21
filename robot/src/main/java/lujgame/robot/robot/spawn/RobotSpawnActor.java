package lujgame.robot.robot.spawn;

import akka.event.LoggingAdapter;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import java.util.ArrayList;
import java.util.List;
import lujgame.core.akka.common.CaseActor;
import lujgame.robot.robot.config.RobotConfigScanner;
import lujgame.robot.robot.config.RobotTemplate;
import lujgame.robot.robot.instance.RobotInstanceActor;
import lujgame.robot.robot.spawn.logic.RobotSpawner;

/**
 * 负责机器人生产和管理
 */
public class RobotSpawnActor extends CaseActor {

  public RobotSpawnActor(RobotSpawner robotSpawner,
      RobotConfigScanner robotConfigScanner) {
    _robotSpawner = robotSpawner;
    _robotConfigScanner = robotConfigScanner;

    _robotList = new ArrayList<>(64);

//    addCase(ChangeRobotCountMsg.class, this::onChangeRobotCount);
  }

  @Override
  public void preStart() throws Exception {
    _eventGroup = new NioEventLoopGroup();

    scanRobot();
  }

  private void scanRobot() {
    RobotSpawner s = _robotSpawner;
    LoggingAdapter log = log();

    //FIXME: 在前一层就应该解析好机器人配置

    List<RobotTemplate> groupList = _robotConfigScanner.scan(log);
    s.spawnRobot(groupList, _eventGroup, getContext(), log);
  }

  private EventLoopGroup _eventGroup;

  private final List<RobotInstanceActor> _robotList;

  private final RobotSpawner _robotSpawner;

  //TODO: 这个应该放到前一层
  private final RobotConfigScanner _robotConfigScanner;
}
