package lujgame.robot.robot.instance;

import akka.actor.ActorRef;
import lujgame.core.akka.common.casev2.ActorCaseHandler;
import lujgame.core.akka.common.casev2.CaseActorV2;
import lujgame.core.akka.common.casev2.CaseContext;

/**
 * 代表一个机器人
 */
public class RobotInstanceActor extends CaseActorV2 {

  public interface Context<M> extends CaseContext<RobotInstanceState, M> {

  }

  public interface Case<M> extends ActorCaseHandler<Context<M>> {

  }

  public enum Start {MSG}

  public enum Behave {MSG}

  public RobotInstanceActor(RobotInstanceState state) {
    _state = state;
  }

  @Override
  public void preStart() throws Exception {
    ActorRef self = getSelf();
    self.tell(Start.MSG, self);
  }

  //TODO: 连接失败时根据配置是否重连

  private final RobotInstanceState _state;
}
