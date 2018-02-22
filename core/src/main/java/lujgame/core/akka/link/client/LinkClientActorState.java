package lujgame.core.akka.link.client;

import akka.actor.ActorRef;

public class LinkClientActorState {

  public String getServerUrl() {
    return _serverUrl;
  }

  public void setServerUrl(String serverUrl) {
    _serverUrl = serverUrl;
  }

  public ActorRef getRequestorRef() {
    return _requestorRef;
  }

  public void setRequestorRef(ActorRef requestorRef) {
    _requestorRef = requestorRef;
  }

  public Enum<?> getSuccessMsg() {
    return _successMsg;
  }

  public void setSuccessMsg(Enum<?> successMsg) {
    _successMsg = successMsg;
  }

  public ActorRef getServerRef() {
    return _serverRef;
  }

  public void setServerRef(ActorRef serverRef) {
    _serverRef = serverRef;
  }

  private String _serverUrl;

  private ActorRef _requestorRef;
  private Enum<?> _successMsg;

  private ActorRef _serverRef;
}
