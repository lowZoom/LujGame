package lujgame.gateway.glue;

import lujgame.core.akka.common.CaseActor;
import lujgame.gateway.glue.message.NewForwardMsg;
import lujgame.gateway.network.akka.accept.logic.ForwardBinder;
import lujgame.gateway.network.akka.accept.message.BindForwardReq;

public class GateGlueActor extends CaseActor {

  public GateGlueActor(
      GateGlueActorState state,
      GlueAdminConnector adminConnector,
      ForwardBinder forwardBinder) {
    _state = state;

    _adminConnector = adminConnector;
    _forwardBinder = forwardBinder;

    addCase(AdminOk.class, this::onAdminConnect);
    addCase(NewForwardMsg.class, this::onNewForward);

    addCase(BindForwardReq.class, this::onBindForward);
  }

  @Override
  public void preStart() throws Exception {
    // 开始尝试连接网关管理节点
    _adminConnector.startConnect(_state, this, AdminOk.MSG);
  }

  private void onAdminConnect(@SuppressWarnings("unused") AdminOk msg) {
    _adminConnector.finishConnect(getSender(), getSelf(), log());
  }

  private void onNewForward(NewForwardMsg msg) {
    _forwardBinder.addForward(_state, msg.getForwardId(), msg.getForwardRef(),log());
  }

  private void onBindForward(BindForwardReq msg) {
    _forwardBinder.findForward(_state, msg.getBoxId(), getSender(), getSelf());
  }

  enum AdminOk {MSG}

  private final GateGlueActorState _state;

  private final GlueAdminConnector _adminConnector;
  private final ForwardBinder _forwardBinder;
}
