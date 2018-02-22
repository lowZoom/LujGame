package lujgame.core.akka.link.server.message;

public class StartListenMsg {

  public StartListenMsg(Enum<?> newLinkMsg) {
    _newLinkMsg = newLinkMsg;
  }

  private final Enum<?> _newLinkMsg;
}
