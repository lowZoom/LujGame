package lujgame.robot.robot.spawn;

import java.util.function.Supplier;
import lujgame.core.akka.common.casev2.CaseActorFactory;
import org.springframework.stereotype.Service;

@Service
public class RobotSpawnActorFactory extends CaseActorFactory<
    RobotSpawnState,
    RobotSpawnActor,
    RobotSpawnActor.Context,
    RobotSpawnActor.Case<?>> {

  @Override
  protected Class<RobotSpawnActor> actorType() {
    return RobotSpawnActor.class;
  }

  @Override
  protected Supplier<RobotSpawnActor> actorConstructor() {
    return RobotSpawnActor::new;
  }

  @Override
  protected Supplier<RobotSpawnActor.Context> contextConstructor() {
    return RobotSpawnActor.Context::new;
  }

  @Override
  protected Class<RobotSpawnActor.PreStart> preStart() {
    return RobotSpawnActor.PreStart.class;
  }
}
