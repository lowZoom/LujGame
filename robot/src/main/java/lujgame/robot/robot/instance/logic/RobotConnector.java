package lujgame.robot.robot.instance.logic;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import javax.inject.Inject;
import lujgame.robot.netty.RobotNettyInit;
import lujgame.robot.robot.config.RobotTemplate;
import lujgame.robot.robot.instance.RobotInstanceActor;
import lujgame.robot.robot.instance.RobotInstanceState;
import org.springframework.stereotype.Service;

@Service
public class RobotConnector {

  public void startConnect(RobotInstanceState state, ActorRef instanceRef, LoggingAdapter log) {
    RobotTemplate template = state.getRobotTemplate();

    String ip = template.getHostname();
    int port = template.getPort();
    log.info("检测目标服务器 -> {}:{}", ip, port);

    new Bootstrap()
        .group(state.getWorkerGroup())
        .channel(NioSocketChannel.class)
//        .option(ChannelOption.SO_KEEPALIVE, true)
        .handler(new RobotNettyInit(instanceRef))
        .connect(ip, port)
//        .addListener(f -> listener.accept(f.isSuccess(), log))
    ;
  }

  public void handleConnectOk(RobotInstanceState state,
      ChannelHandlerContext nettyContext, ActorRef instanceRef, LoggingAdapter log) {
    state.setNettyContext(nettyContext);
    state.getBehaveState().setNettyContext(nettyContext);

    log.debug("连接成功，开始执行行为列表");

    _robotBehaver.initBehave(state.getBehaveState());
    instanceRef.tell(RobotInstanceActor.Behave.MSG, instanceRef);
  }

  public void onConnectDone(boolean success, LoggingAdapter log) {
    log.debug("连接结果：{}", success);
  }

  @Inject
  private RobotBehaver _robotBehaver;
}
