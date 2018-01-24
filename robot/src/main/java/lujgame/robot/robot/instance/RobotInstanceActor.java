package lujgame.robot.robot.instance;

import akka.actor.ActorRef;
import java.util.Map;
import lujgame.core.akka.common.casev2.ActorCaseHandler;
import lujgame.core.akka.common.casev2.CaseActorV2;
import lujgame.core.akka.common.casev2.CaseContext;

/**
 * 代表一个机器人
 */
public class RobotInstanceActor extends CaseActorV2<RobotInstanceState> {

  public static class Context extends CaseContext<RobotInstanceState> {

  }

  public interface Case<M> extends ActorCaseHandler<Context, M> {

  }

  public enum Start {MSG}

  public enum Behave {MSG}

  public RobotInstanceActor(RobotInstanceState state,
      Map<Class<?>, Case> handlerMap) {
    _state = state;
    _handlerMap = handlerMap;
  }

  @Override
  public void preStart() throws Exception {
    ActorRef self = getSelf();
    self.tell(Start.MSG, self);
  }

  @Override
  protected Context createContext() {
    return new Context();
  }

  @Override
  public RobotInstanceState getState() {
    return _state;
  }

  @Override
  protected Map<Class<?>, Case> getHandlerMap() {
    return _handlerMap;
  }

  //TODO: 连接失败时根据配置是否重连

  private final RobotInstanceState _state;

  private final Map<Class<?>, Case> _handlerMap;
}
