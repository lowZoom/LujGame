package lujgame.robot.robot.instance.control.behave;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import lujgame.robot.robot.config.BehaviorConfig;

public class WaitContext {

  public WaitContext(ActorRef robotInstanceRef,
      BehaviorConfig behaviorConfig, LoggingAdapter logger) {
    _robotInstanceRef = robotInstanceRef;
    _behaviorConfig = behaviorConfig;

    _logger = logger;
  }

  public ActorRef getRobotInstanceRef() {
    return _robotInstanceRef;
  }

  public BehaviorConfig getBehaviorConfig() {
    return _behaviorConfig;
  }

  public LoggingAdapter getLogger() {
    return _logger;
  }

  private final ActorRef _robotInstanceRef;
  private final BehaviorConfig _behaviorConfig;

  private final LoggingAdapter _logger;
}
