package lujgame.gateway.glue.cases;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import java.util.Map;
import lujgame.gateway.glue.GateGlueActor;
import lujgame.gateway.glue.GateGlueActorState;
import lujgame.gateway.glue.message.NewForwardMsg;
import org.springframework.stereotype.Service;

@Service
class OnNewForward implements GateGlueActor.Case<NewForwardMsg> {

  @Override
  public void onHandle(GateGlueActor.Context ctx) {
    GateGlueActorState actorState = ctx.getActorState();
    LoggingAdapter log = ctx.getActorLogger();

    NewForwardMsg msg = ctx.getMessage(this);
    addForward(actorState, msg.getForwardId(), msg.getForwardRef(), log);
  }

  /**
   * 增加新的转发节点
   */
  private void addForward(GateGlueActorState state,
      String forwardId, ActorRef forwardRef, LoggingAdapter log) {
    log.info("新的转发节点：{} -> {}", forwardId, forwardRef.path());

    Map<String, ActorRef> forwardMap = state.getForwardMap();
    forwardMap.put(forwardId, forwardRef);
  }
}
