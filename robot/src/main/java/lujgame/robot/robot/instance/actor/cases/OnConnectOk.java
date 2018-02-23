package lujgame.robot.robot.instance.actor.cases;

import akka.event.LoggingAdapter;
import io.netty.channel.ChannelHandlerContext;
import javax.inject.Inject;
import lujgame.core.akka.AkkaTool;
import lujgame.robot.robot.instance.actor.RobotInstanceActor;
import lujgame.robot.robot.instance.actor.message.ConnectOkMsg;
import lujgame.robot.robot.instance.control.state.RobotBehaveState;
import lujgame.robot.robot.instance.control.state.RobotInstanceState;
import org.springframework.stereotype.Service;

@Service
public class OnConnectOk implements RobotInstanceActor.Case<ConnectOkMsg> {

  @Override
  public void onHandle(RobotInstanceActor.Context ctx) {
    RobotInstanceState state = ctx.getActorState();
    ConnectOkMsg msg = ctx.getMessage(this);

    ChannelHandlerContext nettyContext = msg.getNettyContext();
    state.setNettyContext(nettyContext);
    state.getBehaveState().setNettyContext(nettyContext);

    LoggingAdapter log = ctx.getActorLogger();
    log.debug("连接成功，开始执行行为列表");

    initBehave(state.getBehaveState());

    _akkaTool.tellSelf(RobotInstanceActor.Behave.MSG, ctx.getActor());
  }

  private void initBehave(RobotBehaveState state) {
    state.setBehaviorIndex(-1);
//    state.setBehaviorConfig(null);
  }

  @Inject
  private AkkaTool _akkaTool;
}
