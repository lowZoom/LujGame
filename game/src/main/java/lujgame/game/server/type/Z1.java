package lujgame.game.server.type;

import org.springframework.stereotype.Component;

@Component("internal.type")
public class Z1 {

  public JStr newStr(String str) {
    return new JStr(() -> str);
  }

  public JStr.Impl getImpl(JStr str) {
    return str._impl;
  }
}
