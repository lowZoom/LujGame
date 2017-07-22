package lujgame.game.gate;

import lujgame.core.akka.AkkaTool;
import lujgame.core.akka.common.CaseActor;
import lujgame.gateway.glue.message.GateRegisterMsg;

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

    addCase(NewGateConnect.class, this::onNewConnect);
    addCase(GateRegisterMsg.class, this::onNewGate);
  }

  @Override
  public void preStart() throws Exception {
    log().debug("妈了个个个个个个？？？ -> {}", getSelf());
    _akkaTool.linkListen(this, NewGateConnect.MSG);
  }

  private void onNewConnect(@SuppressWarnings("unused") NewGateConnect msg) {
    log().debug("检测到新的网关连接！！@！@");
  }

  /**
   * 有新的网关节点连接过来
   */
  private void onNewGate(@SuppressWarnings("unused") GateRegisterMsg msg) {
    _gateAdder.addGate(_state, getSender(), getSelf(), log());
  }

  enum NewGateConnect {MSG}

  private final CommGateActorState _state;

  private final AkkaTool _akkaTool;
  private final GameGateAdder _gateAdder;
}
