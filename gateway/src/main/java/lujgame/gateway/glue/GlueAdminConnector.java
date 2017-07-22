package lujgame.gateway.glue;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import lujgame.core.akka.AkkaTool;
import lujgame.core.akka.common.CaseActor;
import lujgame.gateway.glue.message.GateRegisterMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GlueAdminConnector {

  @Autowired
  public GlueAdminConnector(AkkaTool akkaTool) {
    _akkaTool = akkaTool;
  }

  public void startConnect(GateGlueActorState state, CaseActor actor, Enum<?> okMsg) {
    String url = state.getGlueUrl();
    _akkaTool.link(actor, url, okMsg);
  }

  public void finishConnect(ActorRef adminRef, ActorRef glueRef, LoggingAdapter log) {
    //TODO: 将自身注册到管理节点
    //TODO: 请求查询投递节点map
    GateRegisterMsg msg = new GateRegisterMsg();
    adminRef.tell(msg, glueRef);

    log.debug("连通！！！！！————————————————————++++++++++");
  }

  private final AkkaTool _akkaTool;
}
