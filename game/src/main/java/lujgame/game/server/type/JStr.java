package lujgame.game.server.type;

import static com.google.common.base.Preconditions.checkNotNull;

public class JStr {

  public interface Impl {

    void setValue(String val);

    String getValue();
  }

  @Override
  public String toString() {
    return String.valueOf(_impl.getValue());
  }

  JStr(Impl impl) {
    _impl = checkNotNull(impl);
  }

  Impl _impl;
}
