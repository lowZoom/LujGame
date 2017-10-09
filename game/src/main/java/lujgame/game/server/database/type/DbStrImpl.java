package lujgame.game.server.database.type;

import lujgame.game.server.type.JStr;

public class DbStrImpl implements JStr.Impl {

  @Override
  public String getValue() {
    return _value;
  }

  private String _value;
}
