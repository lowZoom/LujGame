package lujgame.game.server.type;

import lujgame.core.spring.inject.LujInternal;

@LujInternal
public class Jset0 {

  public <T> JSet<T> newSet(JSet.Impl impl) {
    return new JSet<>(impl);
  }

  public JSet.Impl getImpl(JSet<?> set) {
    return set._impl;
  }
}
