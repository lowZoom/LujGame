package lujgame.robot.boot.actor;

import akka.actor.Props;
import akka.japi.Creator;
import lujgame.robot.robot.spawn.RobotSpawnActorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RobotStartActorFactory {

  @Autowired
  public RobotStartActorFactory(RobotSpawnActorFactory spawnActorFactory) {
    _spawnActorFactory = spawnActorFactory;
  }

  public Props props() {
    Creator<RobotStartActor> c = () -> new RobotStartActor(
        _spawnActorFactory);

    return Props.create(RobotStartActor.class, c);
  }

  private final RobotSpawnActorFactory _spawnActorFactory;
}
