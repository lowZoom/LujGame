package lujgame.robot.robot.instance;

import lujgame.core.akka.common.casev2.ActorCaseHandler;
import lujgame.core.akka.common.casev2.CaseActorContext;
import lujgame.core.akka.common.casev2.CaseActorV2;
import lujgame.core.akka.common.casev2.PreStartHandler;

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

  public enum Behave {MSG}

  //TODO: 连接失败时根据配置是否重连
}
