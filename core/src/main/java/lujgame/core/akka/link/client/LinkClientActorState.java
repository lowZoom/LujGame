package lujgame.core.akka.link.client;

import akka.actor.ActorRef;

public class LinkClientActorState {

  public LinkClientActorState(String linkUrl, ActorRef requestorRef, Enum<?> okMsg) {
    _linkUrl = linkUrl;

    _requestorRef = requestorRef;
    _okMsg = okMsg;
  }

  public ActorRef getLinkRef() {
    return _linkRef;
  }

  public void setLinkRef(ActorRef linkRef) {
    _linkRef = linkRef;
  }

  public String getLinkUrl() {
    return _linkUrl;
  }

  public ActorRef getRequestorRef() {
    return _requestorRef;
  }

  public Enum<?> getOkMsg() {
    return _okMsg;
  }

  private ActorRef _linkRef;

  private final String _linkUrl;

  private final ActorRef _requestorRef;
  private final Enum<?> _okMsg;
}
