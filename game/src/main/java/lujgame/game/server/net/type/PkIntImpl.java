package lujgame.game.server.net.type;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import lujgame.game.server.type.JInt;

public class PkIntImpl implements JInt.Impl {

  public PkIntImpl(IntSupplier getter, IntConsumer setter) {
    _getter = getter;
    _setter = setter;
  }

  @Override
  public void setValue(int val) {
    _setter.accept(val);
  }

  @Override
  public int getValue() {
    return _getter.getAsInt();
  }

  private final IntSupplier _getter;

  private final IntConsumer _setter;
}
