package lujgame.core.akka.link.client.logic;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.UntypedActorContext;
import akka.event.LoggingAdapter;
import java.util.concurrent.TimeUnit;
import lujgame.core.akka.common.CaseActor;
import lujgame.core.akka.link.client.LinkClientActor;
import lujgame.core.akka.link.client.LinkClientActorState;
import lujgame.core.akka.link.message.LinkConnect;
import lujgame.core.akka.schedule.ActorScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LinkConnector {

  @Autowired
  public LinkConnector(
      ActorScheduler actorScheduler) {
    _actorScheduler = actorScheduler;
  }

  public void tryConnect(LinkClientActorState state, CaseActor actor, LoggingAdapter log) {
    String url = state.getLinkUrl();
    log.debug("尝试连接 -> {}", url);

    UntypedActorContext ctx = actor.getContext();
    ActorSelection selection = ctx.actorSelection(url);
    selection.tell(LinkConnect.Try.MSG, ctx.self());

    _actorScheduler.scheduleSelf(actor, 3, TimeUnit.SECONDS,
        ScheduleId.TRY, LinkClientActor.TryConnect.MSG, LinkConnect.Ok.class);
  }

  public void finishConnect(LinkClientActorState state, ActorRef listenRef) {
//    log().debug("link连接成功！！！！！！！！！！！！！！！！！！！");

    ActorRef reqRef = state.getRequestorRef();
    Enum<?> okMsg = state.getOkMsg();

    reqRef.tell(okMsg, listenRef);
  }

  private interface ScheduleId {

    String TRY = LinkClientActor.TryConnect.class.getName();
  }

  private final ActorScheduler _actorScheduler;
}
