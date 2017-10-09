package lujgame.game.server.database.type;

import lujgame.game.server.core.LujInternal;
import lujgame.game.server.type.JStr;
import lujgame.game.server.type.Jstr0;
import org.springframework.beans.factory.annotation.Autowired;

@LujInternal
public class DbTypeInternal {

  public JStr newStr() {
    return _strInternal.newDb();
  }

  @Autowired
  private Jstr0 _strInternal;
}
