package lujgame.game.server.database.type;

import lujgame.game.server.core.LujInternal;
import lujgame.game.server.type.JStr;
import lujgame.game.server.type.JTime;
import lujgame.game.server.type.Jstr0;
import lujgame.game.server.type.Jtime0;
import org.springframework.beans.factory.annotation.Autowired;

@LujInternal
public class DbTypeInternal {

  public JStr newStr() {
    return _strInternal.newDb();
  }

  public JTime newTime() {
    return _timeInternal.newDb();
  }

  @Autowired
  private Jstr0 _strInternal;

  @Autowired
  private Jtime0 _timeInternal;
}
