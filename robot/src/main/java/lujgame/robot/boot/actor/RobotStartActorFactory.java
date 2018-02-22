package lujgame.robot.boot.actor;

import java.util.function.Supplier;
import lujgame.core.akka.common.casev2.CaseActorFactory;
import org.springframework.stereotype.Service;

@Service
public class RobotStartActorFactory extends CaseActorFactory<
    Void,
    RobotStartActor,
    RobotStartActor.Context,
    RobotStartActor.Case<?>> {

  @Override
  protected Class<RobotStartActor> actorType() {
    return RobotStartActor.class;
  }

  @Override
  protected Supplier<RobotStartActor> createActor() {
    return RobotStartActor::new;
  }

  @Override
  protected Supplier<RobotStartActor.Context> createContext() {
    return RobotStartActor.Context::new;
  }

  @Override
  protected Class<RobotStartActor.PreStart> preStart() {
    return RobotStartActor.PreStart.class;
  }
}
