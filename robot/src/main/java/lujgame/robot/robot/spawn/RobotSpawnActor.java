package lujgame.robot.robot.spawn;

import lujgame.core.akka.common.casev2.ActorCaseHandler;
import lujgame.core.akka.common.casev2.CaseActorContext;
import lujgame.core.akka.common.casev2.CaseActorV2;
import lujgame.core.akka.common.casev2.PreStartHandler;

/**
 * 负责机器人生产和管理
 */
public class RobotSpawnActor extends CaseActorV2<RobotSpawnState> {

  public static class Context extends CaseActorContext<RobotSpawnState> {

  }

  public interface Case<M> extends ActorCaseHandler<Context, M> {

  }

  public interface PreStart extends PreStartHandler<Context> {

  }
}
