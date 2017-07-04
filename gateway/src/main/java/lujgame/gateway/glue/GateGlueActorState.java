package lujgame.gateway.glue;

import akka.actor.ActorRef;
import java.util.HashMap;
import java.util.Map;

public class GateGlueActorState {

  public GateGlueActorState(String glueUrl) {
    _glueUrl = glueUrl;

    _forwardMap = new HashMap<>(64);
  }

  public Map<String, ActorRef> getForwardMap() {
    return _forwardMap;
  }

  /**
   * 投递目标节点Map：Id -> Node
   */
  private final Map<String, ActorRef> _forwardMap;

  private final String _glueUrl;
}
