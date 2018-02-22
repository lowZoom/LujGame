package lujgame.core.akka.link.client.message;

public class StartConnectMsg {

  public StartConnectMsg(String linkUrl, Enum<?> successMsg) {
    _linkUrl = linkUrl;
    _successMsg = successMsg;
  }

  public String getLinkUrl() {
    return _linkUrl;
  }

  public Enum<?> getSuccessMsg() {
    return _successMsg;
  }

  private final String _linkUrl;
  private final Enum<?> _successMsg;
}
