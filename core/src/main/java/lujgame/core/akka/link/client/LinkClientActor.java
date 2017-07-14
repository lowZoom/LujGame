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

  @SuppressWarnings("unused")
  private void onTryConnect(TryConnect ignored) {
    _linkConnector.tryConnect(_state, this, log());
  }

  @SuppressWarnings("unused")
  private void onConnectOk(LinkConnect.Ok ignored) {
    log().debug("link连接成功！！！！！！！！！！！！！！！！！！！");
  }

  private final LinkClientActorState _state;

  private final LinkConnector _linkConnector;
}
