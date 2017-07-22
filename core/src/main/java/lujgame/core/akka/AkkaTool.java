package lujgame.core.akka;

import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import lujgame.core.akka.common.CaseActor;
import lujgame.core.akka.link.ActorLinker;
import lujgame.core.akka.schedule.ActorScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AkkaTool {

  @Autowired
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
