package lujgame.game.server.type;

public class JStr {

  public interface Impl {

    String getValue();
  }

  JStr(Impl impl) {
    _impl = impl;
  }

  Impl _impl;
}
