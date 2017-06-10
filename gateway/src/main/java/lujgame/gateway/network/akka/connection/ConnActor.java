package lujgame.gateway.network.akka.connection;

import lujgame.core.akka.CaseActor;

/**
 * 处理一条连接相关逻辑
 */
public class ConnActor extends CaseActor {

  public ConnActor(ConnActorState state) {
    _state = state;
  }

  @Override
  public void preStart() throws Exception {
    log().info("新连接 -> {}", _state.getRemoteAddr());
  }

  private final ConnActorState _state;
}
