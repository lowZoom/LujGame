package lujgame.robot.robot.instance.cases;

import akka.event.LoggingAdapter;
import io.netty.channel.ChannelHandlerContext;
import javax.inject.Inject;
import lujgame.core.akka.AkkaTool;
import lujgame.core.akka.common.casev2.CaseActorV2;
import lujgame.robot.robot.instance.RobotInstanceActor;
import lujgame.robot.robot.instance.RobotInstanceState;
import lujgame.robot.robot.instance.control.RobotBehaver;
import lujgame.robot.robot.instance.message.ConnectOkMsg;
import org.springframework.stereotype.Service;

@Service
class OnConnectOk implements RobotInstanceActor.Case<ConnectOkMsg> {

  @Override
  public void onHandle(RobotInstanceActor.Context ctx) {
    RobotInstanceState state = ctx.getActorState();
    ConnectOkMsg msg = ctx.getMessage(this);

    ChannelHandlerContext nettyContext = msg.getNettyContext();
    state.setNettyContext(nettyContext);
    state.getBehaveState().setNettyContext(nettyContext);

    LoggingAdapter log = ctx.getActorLogger();
    log.debug("连接成功，开始执行行为列表");

    _robotBehaver.initBehave(state.getBehaveState());

    CaseActorV2 instanceActor = ctx.getActor();
    _akkaTool.tellSelf(RobotInstanceActor.Behave.MSG, instanceActor);
  }

  @Inject
  private RobotBehaver _robotBehaver;

  @Inject
  private AkkaTool _akkaTool;
}
