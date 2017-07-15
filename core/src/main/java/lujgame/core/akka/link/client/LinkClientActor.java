package lujgame.core.akka.link.client;

import akka.actor.ActorRef;
import lujgame.core.akka.common.CaseActor;
import lujgame.core.akka.link.client.logic.LinkConnector;
import lujgame.core.akka.link.message.LinkConnect;

/**
 * 让两个actor保持连接
 */
public class LinkClientActor extends CaseActor {

  public enum TryConnect {MSG}

  public LinkClientActor(
      LinkClientActorState state,
      LinkConnector linkConnector) {
    _state = state;
    _linkConnector = linkConnector;

    addCase(TryConnect.class, this::onTryConnect);
    addCase(LinkConnect.Ok.class, this::onConnectOk);
  }

  @Override
  public void preStart() throws Exception {
    ActorRef self = getSelf();
    self.tell(TryConnect.MSG, self);
  }

  private void onTryConnect(@SuppressWarnings("unused") TryConnect msg) {
    _linkConnector.tryConnect(_state, this, log());
  }

  private void onConnectOk(@SuppressWarnings("unused") LinkConnect.Ok msg) {
    _linkConnector.finishConnect(_state, getSelf());
  }

  private final LinkClientActorState _state;

  private final LinkConnector _linkConnector;
}
