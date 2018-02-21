package lujgame.core.akka.link.client.cases;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.LoggingAdapter;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import lujgame.core.akka.link.client.LinkClientActor;
import lujgame.core.akka.link.client.LinkClientActorState;
import lujgame.core.akka.link.message.LinkConnect;
import lujgame.core.akka.schedule.ActorScheduler;
import org.springframework.stereotype.Service;

@Service
public class OnTryConnect implements LinkClientActor.Case<LinkClientActor.TryConnect> {

  @Override
  public void onHandle(LinkClientActor.Context ctx) {
    LinkClientActorState actorState = ctx.getActorState();
    UntypedActor actor = ctx.getActor();

    LoggingAdapter log = ctx.getActorLogger();
    tryConnectServer(actorState, actor, log);
  }

  private void tryConnectServer(LinkClientActorState state,
      UntypedActor clientActor, LoggingAdapter log) {
    String url = state.getLinkUrl();
    log.debug("尝试连接 -> {}", url);

    ActorContext ctx = clientActor.getContext();
    ActorSelection selection = ctx.actorSelection(url);

    ActorRef clientRef = clientActor.getSelf();
    selection.tell(LinkConnect.Try.MSG, clientRef);

    _actorScheduler.scheduleSelf(clientRef, TimeUnit.SECONDS.toMillis(3),
        ScheduleId.TRY, LinkClientActor.TryConnect.MSG);//, LinkConnect.Ok.class);
  }

  private interface ScheduleId {

    String TRY = LinkClientActor.TryConnect.class.getName();
  }

  @Inject
  private ActorScheduler _actorScheduler;
}
