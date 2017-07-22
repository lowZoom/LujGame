package lujgame.core.akka.link.server;

import lujgame.core.akka.common.CaseActor;
import lujgame.core.akka.link.message.LinkConnect;
import lujgame.core.akka.link.server.logic.LinkListener;

public class LinkServerActor extends CaseActor {

  public LinkServerActor(
      LinkServerActorState state,
      LinkListener linkListener) {
    _state = state;
    _linkListener = linkListener;

    addCase(LinkConnect.Try.class, this::onConnectTry);
  }

  private void onConnectTry(@SuppressWarnings("unused") LinkConnect.Try msg) {
    _linkListener.answerConnect(_state, getSender());
  }

  private final LinkServerActorState _state;

  private final LinkListener _linkListener;
}
