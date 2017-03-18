package lujgame.robot.boot.actor;

import akka.actor.Props;
import akka.japi.Creator;
import org.springframework.stereotype.Component;

@Component
public class RobotStartActorFactory {

  public Props props() {
    Creator<RobotStartActor> c = () -> new RobotStartActor();
    return Props.create(RobotStartActor.class, c);
  }
}
