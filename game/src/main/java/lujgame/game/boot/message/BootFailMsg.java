package lujgame.game.boot.message;

public class BootFailMsg {

  public BootFailMsg(String message) {
    _message = message;
  }

  public String getMessage() {
    return _message;
  }

  private final String _message;
}
