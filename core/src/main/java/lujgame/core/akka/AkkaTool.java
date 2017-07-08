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

  public void schedule(CaseActor actor, long len, TimeUnit unit, Object msg, Class<?> interrupt) {
    _actorScheduler.schedule(actor, len, unit, msg, interrupt);
  }

  public void link(CaseActor actor, String linkUrl) {
    _actorLinker.link(actor.getContext(), linkUrl);
  }

  private final ActorScheduler _actorScheduler;
  private final ActorLinker _actorLinker;
}
