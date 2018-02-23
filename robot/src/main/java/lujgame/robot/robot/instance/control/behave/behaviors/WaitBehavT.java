package lujgame.robot.robot.instance.control.behave.behaviors;

import akka.actor.ActorRef;
import javax.inject.Inject;
import lujgame.core.akka.AkkaTool;
import lujgame.robot.robot.config.BehaviorConfig;
import lujgame.robot.robot.instance.actor.RobotInstanceActor;
import lujgame.robot.robot.instance.control.behave.WaitBehavior;
import lujgame.robot.robot.instance.control.behave.WaitContext;
import org.springframework.stereotype.Service;

/**
 * @see BehaviorConfig.Wait#TIME
 */
@Service
public class WaitBehavT implements WaitBehavior {

  @Override
  public void onBehave(WaitContext ctx) {
    ActorRef robotInstanceRef = ctx.getRobotInstanceRef();
    BehaviorConfig behaviorConfig = ctx.getBehaviorConfig();

    long waitMilli = behaviorConfig.getWaitMilli();
    _akkaTool.schedule(robotInstanceRef, waitMilli, ID_BEHAVE, RobotInstanceActor.Behave.MSG);
  }

  private static final String ID_BEHAVE = RobotInstanceActor.Behave.class.getName();

  @Inject
  private AkkaTool _akkaTool;
}
