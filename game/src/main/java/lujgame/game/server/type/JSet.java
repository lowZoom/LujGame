package lujgame.game.server.type;

public class JSet<T> {

  public interface Impl {

  }

  JSet(Impl impl) {
    _impl = impl;
  }

  final Impl _impl;
}
