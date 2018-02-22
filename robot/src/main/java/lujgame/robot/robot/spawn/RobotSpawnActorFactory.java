package lujgame.robot.robot.spawn;

import lujgame.core.akka.common.casev2.CaseActorFactory;
import org.springframework.stereotype.Service;

@Service
public class RobotSpawnActorFactory extends CaseActorFactory<
    RobotSpawnState,
    RobotSpawnActor,
    RobotSpawnActor.Context,
    RobotSpawnActor.Case<?>> {

  @Override
  protected RobotSpawnActor createActor() {
    return new RobotSpawnActor();
  }

  @Override
  protected RobotSpawnActor.Context createContext() {
    return new RobotSpawnActor.Context();
  }

  @Override
  protected Class<RobotSpawnActor.PreStart> preStart() {
    return RobotSpawnActor.PreStart.class;
  }
}
