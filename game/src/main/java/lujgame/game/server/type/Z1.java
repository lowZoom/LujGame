package lujgame.game.server.type;

import java.util.function.Consumer;
import java.util.function.Supplier;
import lujgame.game.server.core.LujInternal;
import lujgame.game.server.net.type.PkStrImpl;

@LujInternal
public class Z1 {

  public JInt newInt(int val) {
    return new JInt(() -> val);
  }

  public JStr newStr(Supplier<String> getter, Consumer<String> setter) {
    return new JStr(new PkStrImpl(getter, setter));
  }

  public JSet.Impl getImpl(JSet<?> set) {
    return set._impl;
  }
}
