package lujgame.robot.robot.instance.control;

import javax.inject.Inject;
import lujgame.core.akka.schedule.ActorScheduler;
import lujgame.robot.robot.instance.control.state.RobotBehaveState;
import org.springframework.stereotype.Service;

@Service
public class RobotBehaver {

  public void initBehave(RobotBehaveState state) {
    state.setBehaviorIndex(-1);
//    state.setBehaviorConfig(null);
  }

  @Inject
  private ActorScheduler _actorScheduler;
}
