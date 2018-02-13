package lujgame.gateway.glue.cases;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.LoggingAdapter;
import lujgame.gateway.glue.GateGlueActor;
import lujgame.gateway.glue.message.GateRegisterMsg;
import org.springframework.stereotype.Service;

/**
 * 成功连上网关中心管理节点
 */
@Service
class OnAdminConnect implements GateGlueActor.Case<GateGlueActor.AdminOk> {

  @Override
  public void onHandle(GateGlueActor.Context ctx) {
    UntypedActor actor = ctx.getActor();
    finishConnect(actor.getSender(), actor.getSelf(), ctx.getActorLogger());
  }

  private void finishConnect(ActorRef adminRef, ActorRef glueRef, LoggingAdapter log) {
    //TODO: 将自身注册到管理节点
    //TODO: 请求查询投递节点map
    GateRegisterMsg msg = new GateRegisterMsg();
    adminRef.tell(msg, glueRef);

    log.debug("连通！！！！！————————————————————++++++++++");
  }
}
