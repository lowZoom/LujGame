package lujgame.game.server.database.type;

import lujgame.game.server.type.JStr;
import org.omg.CORBA.NO_IMPLEMENT;

public class DbStrImpl implements JStr.Impl {

  @Override
  public void setValue(String val) {
    throw new NO_IMPLEMENT("setValue尚未实现");
  }

  @Override
  public String getValue() {
    return _value;
  }

  private String _value;
}
