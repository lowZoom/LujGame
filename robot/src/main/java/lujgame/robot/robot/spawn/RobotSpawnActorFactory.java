package lujgame.robot.robot.spawn;

import akka.actor.Props;
import akka.japi.Creator;
import javax.inject.Inject;
import lujgame.robot.robot.config.RobotConfigScanner;
import lujgame.robot.robot.spawn.logic.RobotSpawner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RobotSpawnActorFactory {

  public Props props() {
    Creator<RobotSpawnActor> c = () -> new RobotSpawnActor(
        _robotSpawner,
        _robotConfigScanner);

    return Props.create(RobotSpawnActor.class, c);
  }

  @Inject
  private RobotSpawner _robotSpawner;

  @Autowired
  private RobotConfigScanner _robotConfigScanner;
}
