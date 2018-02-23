package lujgame.robot.robot.instance.actor.cases;

import static com.google.common.base.Preconditions.checkNotNull;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.LoggingAdapter;
import io.netty.channel.ChannelHandlerContext;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.inject.Inject;
import lujgame.robot.netty.RobotNetPacket;
import lujgame.robot.robot.config.BehaviorConfig;
import lujgame.robot.robot.instance.actor.RobotInstanceActor;
import lujgame.robot.robot.instance.control.behave.WaitBehavMap;
import lujgame.robot.robot.instance.control.behave.WaitBehavior;
import lujgame.robot.robot.instance.control.behave.WaitContext;
import lujgame.robot.robot.instance.control.state.RobotBehaveState;
import org.springframework.stereotype.Service;

@Service
public class OnBehave implements RobotInstanceActor.Case<RobotInstanceActor.Behave> {

  @Override
  public void onHandle(RobotInstanceActor.Context ctx) {
    RobotBehaveState state = ctx.getActorState().getBehaveState();
    UntypedActor instanceActor = ctx.getActor();

    LoggingAdapter log = ctx.getActorLogger();
    handleImpl(state, instanceActor.getSelf(), log);
  }

  private void handleImpl(RobotBehaveState state, ActorRef instanceRef, LoggingAdapter log) {
    // 步进行为游标
    int behaviorIndex = state.getBehaviorIndex() + 1;
    state.setBehaviorIndex(behaviorIndex);

    List<BehaviorConfig> behaviorList = state.getBehaviorList();
    if (behaviorIndex >= behaviorList.size()) {
      log.info("机器人行为结束");
      return;
    }

    ChannelHandlerContext nettyContext = state.getNettyContext();
    BehaviorConfig behaviorCfg = behaviorList.get(behaviorIndex);
    RobotNetPacket packet = encodePacket(behaviorCfg, log);

    log.debug("发送操作码 -> {}", packet.getOpcode());
    nettyContext.writeAndFlush(packet);

    tryWait(instanceRef, behaviorCfg, log);
  }

  private RobotNetPacket encodePacket(BehaviorConfig behaviorCfg, LoggingAdapter log) {
    String dataStr = behaviorCfg.getData();
    log.debug("包 ----> {}", dataStr);

    int opcode = behaviorCfg.getOpcode();
    return new RobotNetPacket(opcode, dataStr.getBytes(StandardCharsets.UTF_8));
  }

  private void tryWait(ActorRef instanceRef, BehaviorConfig behaviorCfg, LoggingAdapter log) {
    BehaviorConfig.Wait waitOption = behaviorCfg.getWaitOption();
    WaitBehavior behavior = _waitBehavMap.getBehavior(waitOption);
    checkNotNull(behavior, waitOption);

    WaitContext ctx = new WaitContext(instanceRef, behaviorCfg, log);
    behavior.onBehave(ctx);
  }

  @Inject
  private WaitBehavMap _waitBehavMap;
}
