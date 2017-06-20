package lujgame.robot.robot.spawn;

import akka.actor.Props;
import akka.japi.Creator;
import java.util.ArrayList;
import lujgame.robot.robot.instance.RobotInstanceActorFactory;
import lujgame.robot.robot.spawn.logic.RobotSpawner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RobotSpawnActorFactory {

  @Autowired
  public RobotSpawnActorFactory(
      RobotSpawner robotSpawner,
      RobotInstanceActorFactory robotInstanceFactory) {
    _robotSpawner = robotSpawner;
    _robotInstanceFactory = robotInstanceFactory;
  }

  public Props props(String ip, int port) {
    Creator<RobotSpawnActor> c = () -> new RobotSpawnActor(
        ip,
        port,
        new ArrayList<>(64),
        _robotSpawner,
        _robotInstanceFactory);

    return Props.create(RobotSpawnActor.class, c);
  }

  private final RobotSpawner _robotSpawner;
  private final RobotInstanceActorFactory _robotInstanceFactory;
}
