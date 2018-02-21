package lujgame.gateway.glue.cases;

import akka.actor.UntypedActor;
import javax.inject.Inject;
import lujgame.core.akka.AkkaTool;
import lujgame.gateway.glue.GateGlueActor;
import lujgame.gateway.glue.GateGlueActorState;
import org.springframework.stereotype.Service;

@Service
public class ActorPreStart implements GateGlueActor.PreStart {

  @Override
  public void preStart(GateGlueActor.Context ctx) throws Exception {
    GateGlueActorState actorState = ctx.getActorState();
    UntypedActor actor = ctx.getActor();

    // 开始尝试连接网关管理节点
    String url = actorState.getGlueUrl();
    _akkaTool.link(actor, url, GateGlueActor.AdminOk.MSG);
  }

  @Inject
  private AkkaTool _akkaTool;
}
