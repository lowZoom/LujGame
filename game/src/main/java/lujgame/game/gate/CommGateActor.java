package lujgame.game.gate;

import lujgame.core.akka.AkkaTool;
import lujgame.core.akka.common.CaseActor;
import lujgame.core.akka.link.message.LinkConnect;

/**
 * 负责与远程网关通讯
 */
public class CommGateActor extends CaseActor {

  public CommGateActor(CommGateActorState state,
      AkkaTool akkaTool,
      GameGateAdder gateAdder) {
    _state = state;

    _akkaTool = akkaTool;
    _gateAdder = gateAdder;
  }

  @Override
  public void preStart() throws Exception {
    log().debug("妈了个个个个个个？？？ -> {}", getSelf());
    _akkaTool.linkListen(this, this::onGateConnect);
  }

  @SuppressWarnings("unused")
  private void onGateConnect(LinkConnect.Try ignored) {
    _gateAdder.addGate(_state, getSender(), getSelf(), log());
  }

  private final CommGateActorState _state;

  private final AkkaTool _akkaTool;
  private final GameGateAdder _gateAdder;
}
