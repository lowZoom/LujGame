package lujgame.robot.boot.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActorContext;
import lujgame.core.akka.CaseActor;
import lujgame.robot.robot.spawn.RobotSpawnActorFactory;
import lujgame.robot.robot.spawn.message.ChangeRobotCountMsg;

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
    String ip = "192.168.0.101";
    int port = 8113;

    UntypedActorContext ctx = getContext();
    Props spawnProps = _spawnActorFactory.props(ip, port);
    ActorRef spawnActor = ctx.actorOf(spawnProps, "Spawn");

    ChangeRobotCountMsg msg = new ChangeRobotCountMsg(1);
    ActorRef self = getSelf();
    spawnActor.tell(msg, self);
  }

  private final RobotSpawnActorFactory _spawnActorFactory;
}
