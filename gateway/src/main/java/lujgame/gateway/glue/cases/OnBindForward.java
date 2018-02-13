package lujgame.gateway.glue.cases;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import java.util.Map;
import lujgame.gateway.glue.GateGlueActor;
import lujgame.gateway.glue.GateGlueActor.Context;
import lujgame.gateway.glue.GateGlueActorState;
import lujgame.gateway.network.akka.accept.message.BindForwardReqLocal;
import lujgame.gateway.network.akka.accept.message.BindForwardReqRemote;
import org.springframework.stereotype.Service;

@Service
class OnBindForward implements GateGlueActor.Case<BindForwardReqLocal> {

  @Override
  public void onHandle(Context ctx) {
    GateGlueActorState actorState = ctx.getActorState();
    UntypedActor actor = ctx.getActor();

    BindForwardReqLocal msg = ctx.getMessage(this);
    findForward(actorState, msg.getForwardId(), msg.getConnId(), actor.getSender());
  }

  /**
   * @param forwardId 在glueActor中查询此id对应转发节点
   */
  private void findForward(GateGlueActorState state,
      String forwardId, String connId, ActorRef connRef) {
    Map<String, ActorRef> forwardMap = state.getForwardMap();
    ActorRef forwardBossRef = forwardMap.get(forwardId);

    //TODO: 判断bossRef为null的情况

    // 将绑定请求转发给游戏服
    BindForwardReqRemote req = new BindForwardReqRemote(connId);
    forwardBossRef.tell(req, connRef);
  }
}
