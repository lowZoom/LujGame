package lujgame.game.gate;

import akka.actor.ActorRef;
import java.util.HashSet;
import java.util.Set;

public class CommGateActorState {

  public CommGateActorState() {
    _gateSet = new HashSet<>(64);
  }

  public Set<ActorRef> getGateSet() {
    return _gateSet;
  }

  /**
   * 网关节点集合
   */
  private final Set<ActorRef> _gateSet;
}
