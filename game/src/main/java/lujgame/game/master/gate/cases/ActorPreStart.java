package lujgame.game.master.gate.cases;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import javax.inject.Inject;
import lujgame.core.akka.AkkaTool;
import lujgame.game.master.gate.CommGateActor;
import org.springframework.stereotype.Service;

@Service
public class ActorPreStart implements CommGateActor.PreStart {

  @Override
  public void preStart(CommGateActor.Context ctx) throws Exception {
    LoggingAdapter log = ctx.getActorLogger();
    ActorRef actorRef = ctx.getActor().getSelf();

    log.debug("妈了个个个个个个？？？ -> {}", actorRef);
    _akkaTool.linkListen(actorRef, CommGateActor.NewGateConnect.MSG);
  }

  @Inject
  private AkkaTool _akkaTool;
}
