package lujgame.core.akka.link.server;

import akka.actor.ActorRef;
import lujgame.core.akka.common.CaseActor;
import lujgame.core.akka.link.message.LinkConnect;

public class LinkServerActor extends CaseActor {

  public LinkServerActor() {
    addCase(LinkConnect.Try.class, this::onConnectTry);
  }

  private void onConnectTry(@SuppressWarnings("unused") LinkConnect.Try ignored) {
    ActorRef clientRef = getSender();
    clientRef.tell(LinkConnect.Ok.MSG, getSelf());
  }
}
