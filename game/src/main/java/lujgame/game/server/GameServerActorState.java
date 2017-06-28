package lujgame.game.server;

public class GameServerActorState {

  public GameServerActorState(String serverId) {
    _serverId = serverId;
  }

  public String getServerId() {
    return _serverId;
  }

  private final String _serverId;
}
