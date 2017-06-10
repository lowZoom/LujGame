package lujgame.robot.robot.spawn;

import akka.actor.Props;
import akka.japi.Creator;
import java.util.ArrayList;
import lujgame.robot.robot.instance.RobotInstanceActorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RobotSpawnActorFactory {

  @Autowired
  public RobotSpawnActorFactory(
      RobotInstanceActorFactory robotInstanceFactory) {
    _robotInstanceFactory = robotInstanceFactory;
  }

  public Props props(String ip, int port) {
    Creator<RobotSpawnActor> c = () -> new RobotSpawnActor(
        ip,
        port,
        new ArrayList<>(64),
        _robotInstanceFactory);

    return Props.create(RobotSpawnActor.class, c);
  }

  private final RobotInstanceActorFactory _robotInstanceFactory;
}
