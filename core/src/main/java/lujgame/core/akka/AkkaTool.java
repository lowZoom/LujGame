package lujgame.core.akka;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import javax.inject.Inject;
import lujgame.core.akka.common.CaseActor;
import lujgame.core.akka.link.ActorLinker;
import lujgame.core.akka.schedule.ActorScheduler;
import org.springframework.stereotype.Service;

@Service
public class AkkaTool {

  @Inject
  public AkkaTool(
      ActorScheduler actorScheduler,
      ActorLinker actorLinker) {
    _actorScheduler = actorScheduler;
    _actorLinker = actorLinker;
  }

  public void subscribeClusterMemberUp(Cluster cluster,
      CaseActor subscriber, Consumer<ClusterEvent.MemberUp> action) {
    subscriber.addCase(ClusterEvent.MemberUp.class, action);

    cluster.subscribe(subscriber.getSelf(),
        ClusterEvent.initialStateAsEvents(), ClusterEvent.MemberUp.class);
  }

  public void tell(Object msg, ActorRef from, ActorRef to) {
    to.tell(msg, from);
  }

  public void tellSelf(Object msg, ActorRef ref) {
    tell(msg, ref, ref);
  }

  public void tellSelf(Object msg, UntypedActor instanceActor) {
    tellSelf(msg, instanceActor.getSelf());
  }

  public void schedule(CaseActor actor, long len, TimeUnit unit,
      String scheduleId, Object msg, Class<?> interrupt) {
    _actorScheduler.scheduleSelf(actor, len, unit, scheduleId, msg, interrupt);
  }

  public void link(CaseActor requestor, String linkUrl, Enum<?> okMsg) {
    _actorLinker.link(linkUrl, requestor, okMsg);
  }

  public void linkListen(CaseActor listener, Enum<?> newMsg) {
    _actorLinker.linkListen(listener, newMsg);
  }

  private final ActorScheduler _actorScheduler;
  private final ActorLinker _actorLinker;
}
