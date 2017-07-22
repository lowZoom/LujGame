package lujgame.game.gate;

import akka.actor.ActorRef;
import java.util.HashSet;
import java.util.Set;

public class CommGateActorState {

  public CommGateActorState(ActorRef masterRef) {
    _masterRef = masterRef;

    _gateSet = new HashSet<>(64);
  }

  public ActorRef getMasterRef() {
    return _masterRef;
  }

  public Set<ActorRef> getGateSet() {
    return _gateSet;
  }

  /**
   * 游戏服分布式管理节点
   */
  private final ActorRef _masterRef;

  /**
   * 网关节点集合
   */
  private final Set<ActorRef> _gateSet;
}
