package lujgame.robot.robot.instance;

import akka.actor.Props;
import akka.japi.Creator;
import io.netty.channel.EventLoopGroup;
import javax.inject.Inject;
import lujgame.robot.robot.config.RobotTemplate;
import lujgame.robot.robot.instance.logic.RobotBehaveState;
import lujgame.robot.robot.instance.logic.RobotBehaver;
import lujgame.robot.robot.instance.logic.RobotConnector;
import org.springframework.stereotype.Service;

@Service
public class RobotInstanceActorFactory {

  public Props props(RobotTemplate robotGroup, EventLoopGroup eventGroup) {
    RobotBehaveState bState = new RobotBehaveState(robotGroup.getBehaviorList());
    RobotInstanceState iState = new RobotInstanceState(robotGroup, eventGroup, bState);

    Creator<RobotInstanceActor> c = () -> new RobotInstanceActor(
        iState,
        _robotConnector,
        _robotBehaver);

    return Props.create(RobotInstanceActor.class, c);
  }

  @Inject
  private RobotConnector _robotConnector;

  @Inject
  private RobotBehaver _robotBehaver;
}
