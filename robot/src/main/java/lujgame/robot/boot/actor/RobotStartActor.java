package lujgame.robot.boot.actor;

import lujgame.core.akka.CaseActor;

/**
 * 机器人启动逻辑
 */
public class RobotStartActor extends CaseActor {

  @Override
  public void preStart() throws Exception {
    log().info("机器人开始启动。。");
  }
}
