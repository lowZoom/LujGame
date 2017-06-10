package lujgame.gateway.network.akka.connection;

import akka.actor.ActorRef;
import java.util.concurrent.TimeUnit;
import lujgame.core.akka.CaseActor;
import lujgame.core.akka.schedule.ActorScheduler;
import lujgame.gateway.network.akka.accept.message.KillConnMsg;

/**
 * 处理一条连接相关逻辑
 */
public class ConnActor extends CaseActor {

  public ConnActor(
      ConnActorState state,
      ActorScheduler actorScheduler) {
    _state = state;
    _actorScheduler = actorScheduler;

    registerMessage();
  }

  @Override
  public void preStart() throws Exception {
    log().info("新连接 -> {}", _state.getRemoteAddr());

    _actorScheduler.schedule(this, 3, TimeUnit.SECONDS, Dumb.MSG);
  }

  private void registerMessage() {
    addCase(Dumb.class, this::onDumb);
  }

  private void onDumb(Dumb ignored) {
    ConnActorState state = _state;
    log().warning("检测到空连接，即将销毁 -> {}", state.getRemoteAddr());

    ActorRef acceptRef = state.getAcceptRef();
    KillConnMsg msg = new KillConnMsg(state.getConnId());
    ActorRef connRef = getSelf();
    acceptRef.tell(msg, connRef);
  }

  enum Dumb {MSG}

  private final ConnActorState _state;

  private final ActorScheduler _actorScheduler;
}
