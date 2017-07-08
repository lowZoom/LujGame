package lujgame.core.akka.link.client;

import akka.actor.ActorRef;
import lujgame.core.akka.common.CaseActor;
import lujgame.core.akka.link.client.logic.LinkConnector;

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
  }

  @Override
  public void preStart() throws Exception {
    log().debug("开始尝试连接 -> {}", _state.getLinkUrl());

    ActorRef self = getSelf();
    self.tell(TryConnect.MSG, self);
  }

  @SuppressWarnings("unused")
  private void onTryConnect(TryConnect ignored) {
    _linkConnector.tryConnect(_state, this, log());
  }

  private final LinkClientActorState _state;

  private final LinkConnector _linkConnector;
}
