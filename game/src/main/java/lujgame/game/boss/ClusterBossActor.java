package lujgame.game.boss;

import akka.actor.ActorRef;
import akka.actor.UntypedActorContext;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.Member;
import lujgame.core.akka.CaseActor;

public class ClusterBossActor extends CaseActor {

  public ClusterBossActor(ClusterBossActorState state, BossServerAdder serverAdder) {
    _state = state;
    _serverAdder = serverAdder;

    addCase(ClusterEvent.MemberUp.class, this::onMemberUp);
  }

  @Override
  public void preStart() throws Exception {
    log().debug("游戏服管理中心启动...");

    Cluster cluster = _state.getCluster();
    ActorRef self = getSelf();
    cluster.subscribe(self, ClusterEvent.initialStateAsEvents(), ClusterEvent.MemberUp.class);
  }

  private void onMemberUp(ClusterEvent.MemberUp msg) {
    BossServerAdder a = _serverAdder;
    Member member = msg.member();
    if (!a.isGameServer(member)) {
      return;
    }

    UntypedActorContext ctx = getContext();
    a.addServer(ctx, member, log());
  }

  private final ClusterBossActorState _state;

  private final BossServerAdder _serverAdder;
}
