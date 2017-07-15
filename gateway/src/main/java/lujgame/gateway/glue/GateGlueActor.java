package lujgame.gateway.glue;

import lujgame.core.akka.common.CaseActor;
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

    addCase(BindForwardReq.class, this::onBindForward);
    addCase(AdminOk.class, this::onAdminConnect);
  }

  @Override
  public void preStart() throws Exception {
    // 开始尝试连接网关管理节点
    _adminConnector.connectAdmin(_state, this, AdminOk.MSG);
  }

  private void onBindForward(BindForwardReq msg) {
    _forwardBinder.findForward(_state, msg.getBoxId(), getSender(), getSelf());
  }

  private void onAdminConnect(@SuppressWarnings("unused") AdminOk msg) {
    //TODO: 请求查询投递节点map

    log().debug("连通！！！！！————————————————————++++++++++");
  }

  enum AdminOk {MSG}

  private final GateGlueActorState _state;

  private final GlueAdminConnector _adminConnector;
  private final ForwardBinder _forwardBinder;
}
