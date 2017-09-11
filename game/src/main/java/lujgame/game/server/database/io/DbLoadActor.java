package lujgame.game.server.database.io;

import lujgame.core.akka.common.CaseActor;

public class DbLoadActor extends CaseActor {

  public DbLoadActor(DbLoadActorState state) {
    _state = state;
  }

  private final DbLoadActorState _state;
}
