package lujgame.core.akka;

import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import java.util.function.Consumer;
import org.springframework.stereotype.Component;

@Component
public class AkkaTool {

  public void subscribeClusterMemberUp(Cluster cluster,
      CaseActor subscriber, Consumer<ClusterEvent.MemberUp> action) {
    subscriber.addCase(ClusterEvent.MemberUp.class, action);

    cluster.subscribe(subscriber.getSelf(),
        ClusterEvent.initialStateAsEvents(), ClusterEvent.MemberUp.class);
  }
}
