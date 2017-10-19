package lujgame.game.server.type;

import lujgame.game.server.core.LujInternal;

@LujInternal
public class Jset0 {

  public <T> JSet<T> newSet(JSet.Impl impl) {
    return new JSet<>(impl);
  }

  public JSet.Impl getImpl(JSet<?> set) {
    return set._impl;
  }
}
