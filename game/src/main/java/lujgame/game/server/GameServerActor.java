package lujgame.game.server;

import lujgame.core.akka.CaseActor;

public class GameServerActor extends CaseActor {

  public GameServerActor(GameServerActorState state) {
    _state = state;
  }

  @Override
  public void preStart() throws Exception {
    log().debug("游戏服启动，ID：{}", _state.getServerId());
  }

  //TODO: 有种子节点UP，就把自己注册过去

  private final GameServerActorState _state;
}
