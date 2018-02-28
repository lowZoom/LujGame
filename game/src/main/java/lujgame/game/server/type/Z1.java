package lujgame.game.server.type;

import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import lujgame.core.spring.inject.LujInternal;
import lujgame.game.server.net.type.PkIntImpl;
import lujgame.game.server.net.type.PkStrImpl;

@LujInternal
public class Z1 { // 因为要注入到生成类里，所以统一到一个类里，方便生成

  public JInt newInt(IntSupplier getter, IntConsumer setter) {
    return new JInt(new PkIntImpl(getter, setter));
  }

  public JStr newStr(Supplier<String> getter, Consumer<String> setter) {
    return new JStr(new PkStrImpl(getter, setter));
  }
}
