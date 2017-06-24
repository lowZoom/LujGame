package lujgame.robot.robot.instance;

import lujgame.core.akka.CaseActor;
import lujgame.robot.robot.instance.logic.RobotConnector;

/**
 * 代表一个机器人
 */
public class RobotInstanceActor extends CaseActor {

  public RobotInstanceActor(
      RobotInstanceState state,
      RobotConnector robotConnector) {
    _state = state;
    _robotConnector = robotConnector;
  }

  @Override
  public void preStart() throws Exception {
    RobotConnector c = _robotConnector;
    c.startConnect(_state, c::onConnectDone, log());
  }

  private final RobotInstanceState _state;

  private final RobotConnector _robotConnector;
}
