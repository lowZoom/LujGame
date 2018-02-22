package lujgame.robot.robot.instance.actor;

import lujgame.core.akka.common.casev2.CaseActorFactory;
import lujgame.robot.robot.instance.control.state.RobotInstanceState;
import org.springframework.stereotype.Service;

@Service
public class RobotInstanceActorFactory extends CaseActorFactory<
    RobotInstanceState,
    RobotInstanceActor,
    RobotInstanceActor.Context,
    RobotInstanceActor.Case<?>> {

  @Override
  protected RobotInstanceActor createActor() {
    return new RobotInstanceActor();
  }

  @Override
  protected RobotInstanceActor.Context createContext() {
    return new RobotInstanceActor.Context();
  }

  @Override
  protected Class<RobotInstanceActor.PreStart> preStart() {
    return RobotInstanceActor.PreStart.class;
  }
}
