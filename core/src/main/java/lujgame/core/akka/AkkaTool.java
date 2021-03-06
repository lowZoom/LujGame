package lujgame.core.akka;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import java.util.function.Consumer;
import javax.inject.Inject;
import lujgame.core.akka.common.CaseActor;
import lujgame.core.akka.link.ActorLinker;
import lujgame.core.akka.schedule.ActorScheduler;
import org.springframework.stereotype.Service;

@Service
public class AkkaTool {

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

  public void schedule(ActorRef requestor, long delay, String scheduleId, Object msg) {
    _actorScheduler.scheduleSelf(requestor, delay, scheduleId, msg);
  }

  public void link(ActorRef requestor, String linkUrl, Enum<?> okMsg) {
    _actorLinker.link(linkUrl, requestor, okMsg);
  }

  public void linkListen(ActorRef listener, Enum<?> newMsg) {
    _actorLinker.linkListen(listener, newMsg);
  }

  @Inject
  private ActorScheduler _actorScheduler;

  @Inject
  private ActorLinker _actorLinker;
}
