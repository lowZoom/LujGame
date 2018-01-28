package lujgame.robot.robot.instance.cases;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.socket.nio.NioSocketChannel;
import lujgame.robot.netty.RobotNettyInit;
import lujgame.robot.robot.config.RobotTemplate;
import lujgame.robot.robot.instance.RobotInstanceActor;
import lujgame.robot.robot.instance.RobotInstanceState;
import org.springframework.stereotype.Service;

@Service("RobotInstanceActor.PreStart")
class ActorPreStart implements RobotInstanceActor.PreStart {

  @Override
  public void preStart(RobotInstanceActor.Context ctx) throws Exception {
    RobotInstanceState state = ctx.getActorState();
    ActorRef instanceRef = ctx.getActor().getSelf();

    LoggingAdapter log = ctx.getActorLogger();
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
        .addListener(f -> onConnectDone(f.isSuccess(), log))
    ;
  }

  private void onConnectDone(boolean success, LoggingAdapter log) {
    log.debug("连接结果：{}", success);
  }
}
