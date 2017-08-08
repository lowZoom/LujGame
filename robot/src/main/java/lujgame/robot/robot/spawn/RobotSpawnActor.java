package lujgame.robot.robot.spawn;

import akka.event.LoggingAdapter;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import lujgame.core.akka.common.CaseActor;
import lujgame.robot.robot.instance.RobotInstanceActor;
import lujgame.robot.robot.instance.RobotInstanceActorFactory;
import lujgame.robot.robot.spawn.logic.RobotGroup;
import lujgame.robot.robot.spawn.logic.RobotSpawner;

/**
 * 负责机器人生产和管理
 */
public class RobotSpawnActor extends CaseActor {

  public RobotSpawnActor(
      RobotSpawner robotSpawner,
      RobotInstanceActorFactory robotInstanceFactory) {
    _robotSpawner = robotSpawner;
    _robotInstanceFactory = robotInstanceFactory;

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

    List<Path> configList = s.findRobotConfig("robot", log);
    List<RobotGroup> groupList = s.makeRobotGroup(configList, log);
    s.spawnRobot(groupList, _eventGroup, getContext(), log);
  }

  private EventLoopGroup _eventGroup;

  private final List<RobotInstanceActor> _robotList;

  private final RobotSpawner _robotSpawner;
  private final RobotInstanceActorFactory _robotInstanceFactory;
}
