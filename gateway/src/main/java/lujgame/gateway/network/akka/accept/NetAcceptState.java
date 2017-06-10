package lujgame.gateway.network.akka.accept;

import akka.actor.ActorRef;
import java.util.HashMap;
import java.util.Map;

public class NetAcceptState {

  public NetAcceptState() {
    _connectionMap = new HashMap<>(1024);
  }

  private final Map<String, ActorRef> _connectionMap;
}
