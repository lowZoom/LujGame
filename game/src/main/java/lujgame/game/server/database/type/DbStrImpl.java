package lujgame.game.server.database.type;

import java.util.Objects;
import lujgame.game.server.type.JStr;

public class DbStrImpl implements JStr.Impl {

  @Override
  public void setValue(String val) {
    if (Objects.equals(_value, val)) {
      _mod = null;
      return;
    }

    startModify().setValue(val);
  }

  @Override
  public String getValue() {
    return (_mod != null) ? _mod.getValue() : _value;
  }

  private DbStrMod startModify() {
    return (_mod != null) ? _mod : (_mod = new DbStrMod());
  }

  private String _value;

  private DbStrMod _mod;
}
