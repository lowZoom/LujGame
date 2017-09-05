package lujgame.game.server.type;

import org.springframework.stereotype.Component;

@Component("internal.type")
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
}
