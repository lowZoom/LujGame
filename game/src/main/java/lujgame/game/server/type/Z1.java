package lujgame.game.server.type;

import lujgame.game.server.core.LujInternal;

@LujInternal
public class Z1 {

  public JInt newInt(int val) {
    return new JInt(() -> val);
  }

  public JStr newStr(String val) {
    return new JStr(() -> val);
  }

  public JStr.Impl getImpl(JStr str) {
    return str._impl;
  }

  public JSet.Impl getImpl(JSet<?> set) {
    return set._impl;
  }
}
