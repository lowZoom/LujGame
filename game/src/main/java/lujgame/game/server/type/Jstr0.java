package lujgame.game.server.type;

import lujgame.game.server.core.LujInternal;
import lujgame.game.server.database.type.DbStrImpl;

@LujInternal
public class Jstr0 {

  public JStr newDb() {
    return new JStr(new DbStrImpl());
  }

  public JStr.Impl getImpl(JStr str) {
    return str._impl;
  }
}
