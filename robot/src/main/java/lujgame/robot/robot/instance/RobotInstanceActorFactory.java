package lujgame.robot.robot.instance;

import akka.actor.Props;
import akka.japi.Creator;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.netty.channel.EventLoopGroup;
import java.nio.file.Path;
import lujgame.core.file.FileTool;
import lujgame.robot.robot.instance.logic.RobotBehaveState;
import lujgame.robot.robot.instance.logic.RobotBehaver;
import lujgame.robot.robot.instance.logic.RobotConnector;
import lujgame.robot.robot.spawn.logic.RobotGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RobotInstanceActorFactory {

  @Autowired
  public RobotInstanceActorFactory(
      FileTool fileTool,
      RobotConnector robotConnector,
      RobotBehaver robotBehaver) {
    _fileTool = fileTool;

    _robotConnector = robotConnector;
    _robotBehaver = robotBehaver;
  }

  public RobotGroup readGroup(Path path) {
    String groupName = _fileTool.getName(path.toString());
    Config config = ConfigFactory.parseFile(path.toFile());

    return new RobotGroup(groupName, config.getConfig("robot"));
  }

  public Props props(RobotGroup robotGroup, EventLoopGroup eventGroup) {
    RobotBehaveState bState = new RobotBehaveState();

    RobotInstanceState iState = new RobotInstanceState(robotGroup, eventGroup, bState);

    Creator<RobotInstanceActor> c = () -> new RobotInstanceActor(
        iState,
        _robotConnector,
        _robotBehaver);

    return Props.create(RobotInstanceActor.class, c);
  }

  private final FileTool _fileTool;

  private final RobotConnector _robotConnector;
  private final RobotBehaver _robotBehaver;
}
