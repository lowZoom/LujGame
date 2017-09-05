package lujgame.game.server.type;

import static com.google.common.base.Preconditions.checkNotNull;

public class JInt {

  public interface Impl {

    int getValue();
  }

  @Override
  public String toString() {
    return Integer.toString(_impl.getValue());
  }

  JInt(JInt.Impl impl) {
    _impl = checkNotNull(impl);
  }

  Impl _impl;
}
