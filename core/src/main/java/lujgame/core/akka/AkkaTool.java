package lujgame.core.akka;

import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import java.util.function.Consumer;
import lujgame.core.akka.common.CaseActor;
import lujgame.core.akka.link.logic.ActorLinker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AkkaTool {

  @Autowired
  public AkkaTool(ActorLinker actorLinker) {
    _actorLinker = actorLinker;
  }

  public void subscribeClusterMemberUp(Cluster cluster,
      CaseActor subscriber, Consumer<ClusterEvent.MemberUp> action) {
    subscriber.addCase(ClusterEvent.MemberUp.class, action);

    cluster.subscribe(subscriber.getSelf(),
        ClusterEvent.initialStateAsEvents(), ClusterEvent.MemberUp.class);
  }

  public void link(CaseActor actor, String linkUrl) {
    _actorLinker.link(actor.getContext(), linkUrl);
  }

  private final ActorLinker _actorLinker;
}
