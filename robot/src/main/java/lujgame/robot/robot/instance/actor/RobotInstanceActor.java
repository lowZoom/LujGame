package lujgame.robot.robot.instance.actor;

import lujgame.core.akka.common.casev2.ActorCaseHandler;
import lujgame.core.akka.common.casev2.CaseActorContext;
import lujgame.core.akka.common.casev2.CaseActorV2;
import lujgame.core.akka.common.casev2.PreStartHandler;
import lujgame.robot.robot.instance.control.state.RobotInstanceState;

/**
 * 代表一个机器人
 */
public class RobotInstanceActor extends CaseActorV2<RobotInstanceState> {

  public static class Context extends CaseActorContext<RobotInstanceState> {

  }

  public interface Case<M> extends ActorCaseHandler<Context, M> {

  }

  public interface PreStart extends PreStartHandler<Context> {

  }

  public enum BehaveNext {MSG}

  //TODO: 连接失败时根据配置是否重连
}
