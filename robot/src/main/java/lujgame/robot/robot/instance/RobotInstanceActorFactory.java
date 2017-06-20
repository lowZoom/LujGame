package lujgame.robot.robot.instance;

import akka.actor.Props;
import akka.japi.Creator;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.netty.channel.EventLoopGroup;
import java.nio.file.Path;
import lujgame.core.file.FileTool;
import lujgame.robot.robot.spawn.logic.RobotGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RobotInstanceActorFactory {

  @Autowired
  public RobotInstanceActorFactory(FileTool fileTool) {
    _fileTool = fileTool;
  }

  public RobotGroup readGroup(Path path) {
    String groupName = _fileTool.getName(path.toString());
    Config config = ConfigFactory.parseFile(path.toFile());

    return new RobotGroup(groupName, config);
  }

  public boolean validateGroup(RobotGroup group) {
    Config config = group.getConfig();
    String name = group.getName();

    return config.hasPath(name);
  }

  public Props props(EventLoopGroup eventGroup, String ip, int port) {
    Creator<RobotInstanceActor> c = () -> new RobotInstanceActor(
        eventGroup, ip, port);

    return Props.create(RobotInstanceActor.class, c);
  }

  private final FileTool _fileTool;
}
