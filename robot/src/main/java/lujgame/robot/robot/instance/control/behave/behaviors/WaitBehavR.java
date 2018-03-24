package lujgame.robot.robot.instance.control.behave.behaviors;

import akka.event.LoggingAdapter;
import lujgame.robot.robot.config.BehaviorConfig;
import lujgame.robot.robot.instance.control.behave.WaitBehavior;
import lujgame.robot.robot.instance.control.behave.WaitContext;
import org.springframework.stereotype.Service;

/**
 * @see BehaviorConfig.Wait#RESPONSE
 */
@Service
public class WaitBehavR implements WaitBehavior {

  @Override
  public void onBehave(WaitContext ctx) {
    LoggingAdapter log = ctx.getLogger();
    BehaviorConfig behavCfg = ctx.getBehaviorConfig();

    log.debug("等待响应：{}", behavCfg.getOpcode());

    //TODO: 设置成正在等待响应
  }
}
