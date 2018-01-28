package lujgame.robot.boot.actor.cases;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActorContext;
import akka.event.LoggingAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import lujgame.robot.boot.actor.RobotStartActor;
import lujgame.robot.robot.config.RobotConfigScanner;
import lujgame.robot.robot.config.RobotTemplate;
import lujgame.robot.robot.spawn.RobotSpawnActorFactory;
import lujgame.robot.robot.spawn.RobotSpawnState;
import org.springframework.stereotype.Service;

/**
 * 机器人程序入口逻辑
 */
@Service("RobotStartActor.PreStart")
class ActorPreStart implements RobotStartActor.PreStart {

  @Override
  public void preStart(RobotStartActor.Context ctx) throws Exception {
    LoggingAdapter log = ctx.getActorLogger();
    log.info("机器人程序启动。。");

    List<RobotTemplate> templateList = _robotConfigScanner.scan(log);
    startSpawn(templateList, ctx.getActor().getContext());
  }

  private void startSpawn(List<RobotTemplate> templateList, UntypedActorContext ctx) {
    RobotSpawnState state = new RobotSpawnState(templateList, new ArrayList<>(64));
    Props spawnProps = _spawnActorFactory.props(state);

    ActorRef spawnActor = ctx.actorOf(spawnProps, "Spawn");

//    ChangeRobotCountMsg msg = new ChangeRobotCountMsg(1);
//    ActorRef self = getSelf();
//    spawnActor.tell(msg, self);
  }

  @Inject
  private RobotConfigScanner _robotConfigScanner;

  @Inject
  private RobotSpawnActorFactory _spawnActorFactory;
}
