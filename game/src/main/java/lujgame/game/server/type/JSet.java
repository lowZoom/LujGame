package lujgame.game.server.type;

public class JSet<T> {

  public interface Impl {

  }

  public JSet(Impl impl) {
    _impl = impl;
  }

  final Impl _impl;
}
