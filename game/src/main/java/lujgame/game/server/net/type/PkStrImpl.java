package lujgame.game.server.net.type;

import java.util.function.Consumer;
import java.util.function.Supplier;
import lujgame.game.server.type.JStr;

public class PkStrImpl implements JStr.Impl {

  public PkStrImpl(Supplier<String> getter, Consumer<String> setter) {
    _getter = getter;
    _setter = setter;
  }

  @Override
  public void setValue(String val) {
    _setter.accept(val);
  }

  @Override
  public String getValue() {
    return _getter.get();
  }

  private final Supplier<String> _getter;

  private final Consumer<String> _setter;
}
