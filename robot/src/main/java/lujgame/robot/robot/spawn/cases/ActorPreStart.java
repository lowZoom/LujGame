package lujgame.robot.robot.spawn.cases;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActorContext;
import akka.event.LoggingAdapter;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import java.util.List;
import javax.inject.Inject;
import lujgame.robot.robot.config.RobotConfigScanner;
import lujgame.robot.robot.config.RobotTemplate;
import lujgame.robot.robot.instance.RobotInstanceActorFactory;
import lujgame.robot.robot.instance.RobotInstanceState;
import lujgame.robot.robot.instance.control.RobotBehaveState;
import lujgame.robot.robot.spawn.RobotSpawnActor;
import lujgame.robot.robot.spawn.RobotSpawnState;
import org.springframework.stereotype.Service;

@Service("RobotSpawnActor.PreStart")
class ActorPreStart implements RobotSpawnActor.PreStart {

  @Override
  public void preStart(RobotSpawnActor.Context ctx) throws Exception {
    RobotSpawnState state = ctx.getActorState();

    NioEventLoopGroup loopGroup = new NioEventLoopGroup();
    state.setEventLoopGroup(loopGroup);

    scanRobot(loopGroup, ctx.getActor().getContext(), ctx.getActorLogger());
  }

  private void scanRobot(NioEventLoopGroup loopGroup,
      UntypedActorContext actorCtx, LoggingAdapter log) {
    //FIXME: 在前一层就应该解析好机器人配置

    List<RobotTemplate> groupList = _robotConfigScanner.scan(log);
    for (RobotTemplate group : groupList) {
      log.debug("启动机器人组 -> {}", group.getTemplateName());
      spawnGroup(group, loopGroup, actorCtx);
    }
  }

  private void spawnGroup(RobotTemplate robotGroup,
      EventLoopGroup eventGroup, UntypedActorContext actorCtx) {
    int num = robotGroup.getAmount();
    for (int i = 0; i < num; i++) {
      spawnOneRobot(actorCtx, robotGroup, eventGroup);
    }
  }

  private void spawnOneRobot(UntypedActorContext actorCtx, RobotTemplate robotGroup,
      EventLoopGroup eventGroup) {
    RobotBehaveState bState = new RobotBehaveState(robotGroup.getBehaviorList());
    RobotInstanceState iState = new RobotInstanceState(robotGroup, eventGroup, bState);

    Props props = _robotInstanceFactory.props(iState);
    ActorRef instanceRef = actorCtx.actorOf(props);
  }

  @Inject
  private RobotInstanceActorFactory _robotInstanceFactory;

  //TODO: 这个应该放到前一层
  @Inject
  private RobotConfigScanner _robotConfigScanner;
}
