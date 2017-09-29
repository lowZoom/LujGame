package lujgame.game.server.database.type;

import java.util.Set;

public class IdSet {

  public IdSet(Set<DbId> setImpl) {
    _setImpl = setImpl;
  }

  public Iterable<DbId> iter() {
    return _setImpl;
  }

  private final Set<DbId> _setImpl;
}
