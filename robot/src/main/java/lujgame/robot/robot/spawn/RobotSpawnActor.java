package lujgame.robot.robot.spawn;

import akka.actor.Props;
import akka.actor.UntypedActorContext;
import akka.event.LoggingAdapter;
import com.typesafe.config.Config;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import java.nio.file.Path;
import java.util.List;
import lujgame.core.akka.CaseActor;
import lujgame.robot.robot.instance.RobotInstanceActor;
import lujgame.robot.robot.instance.RobotInstanceActorFactory;
import lujgame.robot.robot.spawn.logic.RobotSpawner;
import lujgame.robot.robot.spawn.message.ChangeRobotCountMsg;

/**
 * 负责机器人生产和管理
 */
public class RobotSpawnActor extends CaseActor {

  public RobotSpawnActor(String ip, int port,
      List<RobotInstanceActor> robotList,
      RobotSpawner robotSpawner, RobotInstanceActorFactory robotInstanceFactory) {
    _ip = ip;
    _port = port;

    _robotList = robotList;

    _robotSpawner = robotSpawner;
    _robotInstanceFactory = robotInstanceFactory;

    addCase(ChangeRobotCountMsg.class, this::onChangeRobotCount);
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
    s.spawnRobot(configList, log);
  }

  private void onChangeRobotCount(ChangeRobotCountMsg msg) {
    int oldCount = _robotList.size();
    int newCount = msg.getCount();

    int delta = newCount - oldCount;
    if (delta == 0) {
      return;
    }

    log().info("更改机器人数量 -> {}", newCount);

    UntypedActorContext ctx = getContext();
    Props robotProps = _robotInstanceFactory.props(_eventGroup, _ip, _port);
    ctx.actorOf(robotProps, "Robot");
  }

  private EventLoopGroup _eventGroup;

  private final String _ip;
  private final int _port;

  private final List<RobotInstanceActor> _robotList;

  private final RobotSpawner _robotSpawner;
  private final RobotInstanceActorFactory _robotInstanceFactory;
}
