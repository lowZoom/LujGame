package lujgame.robot.boot.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActorContext;
import java.util.ArrayList;
import lujgame.core.akka.common.CaseActor;
import lujgame.robot.robot.spawn.RobotSpawnActorFactory;
import lujgame.robot.robot.spawn.RobotSpawnState;

/**
 * 机器人启动逻辑
 */
public class RobotStartActor extends CaseActor {

  public RobotStartActor(RobotSpawnActorFactory spawnActorFactory) {
    _spawnActorFactory = spawnActorFactory;
  }

  @Override
  public void preStart() throws Exception {
    log().info("机器人程序启动。。");

    loadConfig();
  }

  private void loadConfig() {
    RobotSpawnState state = new RobotSpawnState(new ArrayList<>(64));
    Props spawnProps = _spawnActorFactory.props(state);

    UntypedActorContext ctx = getContext();
    ActorRef spawnActor = ctx.actorOf(spawnProps, "Spawn");

//    ChangeRobotCountMsg msg = new ChangeRobotCountMsg(1);
//    ActorRef self = getSelf();
//    spawnActor.tell(msg, self);
  }

  private final RobotSpawnActorFactory _spawnActorFactory;
}
