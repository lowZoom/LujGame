package lujgame.robot.boot.actor;

import lujgame.core.akka.common.casev2.CaseActorFactory;
import org.springframework.stereotype.Service;

@Service
public class RobotStartActorFactory extends CaseActorFactory<
    Void,
    RobotStartActor,
    RobotStartActor.Context,
    RobotStartActor.Case<?>> {

  @Override
  protected RobotStartActor createActor() {
    return new RobotStartActor();
  }

  @Override
  protected RobotStartActor.Context createContext() {
    return new RobotStartActor.Context();
  }

  @Override
  protected Class<RobotStartActor.PreStart> preStart() {
    return RobotStartActor.PreStart.class;
  }
}
