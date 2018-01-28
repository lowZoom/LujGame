package lujgame.robot.robot.instance;

import java.util.function.Supplier;
import lujgame.core.akka.common.casev2.CaseActorFactory;
import org.springframework.stereotype.Service;

@Service
public class RobotInstanceActorFactory extends CaseActorFactory<
    RobotInstanceState,
    RobotInstanceActor,
    RobotInstanceActor.Context,
    RobotInstanceActor.Case<?>> {

  @Override
  protected Class<RobotInstanceActor> actorType() {
    return RobotInstanceActor.class;
  }

  @Override
  protected Supplier<RobotInstanceActor> actorConstructor() {
    return RobotInstanceActor::new;
  }

  @Override
  protected Supplier<RobotInstanceActor.Context> contextConstructor() {
    return RobotInstanceActor.Context::new;
  }

  @Override
  protected Class<RobotInstanceActor.PreStart> preStart() {
    return RobotInstanceActor.PreStart.class;
  }
}
