package lujgame.robot.robot.instance;

import akka.actor.ActorRef;
import io.netty.channel.ChannelHandlerContext;
import lujgame.core.akka.CaseActor;
import lujgame.robot.robot.instance.logic.RobotConnector;
import lujgame.robot.robot.instance.message.ConnectOkMsg;

/**
 * 代表一个机器人
 */
public class RobotInstanceActor extends CaseActor {

  public RobotInstanceActor(
      RobotInstanceState state,
      RobotConnector robotConnector) {
    _state = state;
    _robotConnector = robotConnector;

    registerMessage();
  }

  @Override
  public void preStart() throws Exception {
    ActorRef self = getSelf();
    _robotConnector.startConnect(_state, self, log());
  }

  private void registerMessage() {
    addCase(ConnectOkMsg.class, this::onConnectOk);
//    addCase(ConnectFail.class, this::onConnectFail);
  }

  private void onConnectOk(ConnectOkMsg msg) {
    ChannelHandlerContext nettyCtx = msg.getNettyContext();
    _robotConnector.handleConnectOk(_state, nettyCtx, log());
  }

  //TODO: 连接失败时根据配置是否重连

  private final RobotInstanceState _state;

  private final RobotConnector _robotConnector;
}
