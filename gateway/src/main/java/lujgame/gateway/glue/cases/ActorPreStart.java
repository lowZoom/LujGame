package lujgame.gateway.glue.cases;

import akka.actor.UntypedActor;
import javax.inject.Inject;
import lujgame.core.akka.AkkaTool;
import lujgame.gateway.glue.GateGlueActor;
import lujgame.gateway.glue.GateGlueActorState;
import org.springframework.stereotype.Service;

@Service
class ActorPreStart implements GateGlueActor.PreStart {

  @Override
  public void preStart(GateGlueActor.Context ctx) throws Exception {
    GateGlueActorState actorState = ctx.getActorState();
    UntypedActor actor = ctx.getActor();

    // 开始尝试连接网关管理节点
    startConnect(actorState, actor, GateGlueActor.AdminOk.MSG);
  }

  private void startConnect(GateGlueActorState state, UntypedActor actor, Enum<?> okMsg) {
    String url = state.getGlueUrl();
    _akkaTool.link(actor, url, okMsg);
  }

  @Inject
  private AkkaTool _akkaTool;
}
