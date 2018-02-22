package lujgame.game.master.gate.cases;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.LoggingAdapter;
import java.util.Set;
import lujgame.game.master.cluster.message.ReplyGateMsg;
import lujgame.game.master.gate.CommGateActor;
import lujgame.game.master.gate.CommGateActorState;
import lujgame.gateway.glue.message.GateRegisterMsg;
import org.springframework.stereotype.Service;

/**
 * 有新的网关节点连接过来
 */
@Service
public class OnGateRegister implements CommGateActor.Case<GateRegisterMsg> {

  @Override
  public void onHandle(CommGateActor.Context ctx) {
    CommGateActorState actorState = ctx.getActorState();
    UntypedActor actor = ctx.getActor();

    LoggingAdapter log = ctx.getActorLogger();
    addGate(actorState, actor.getSender(), actor.getSelf(), log);
  }

  private void addGate(CommGateActorState state,
      ActorRef gateRef, ActorRef commRef, LoggingAdapter log) {
    Set<ActorRef> gateSet = state.getGateSet();

    // 已经存在则忽略
    if (!gateSet.add(gateRef)) {
      return;
    }

    log.info("新的网关连接 -> {}", gateRef.path());

    ActorRef masterRef = state.getMasterRef();
    ReplyGateMsg msg = new ReplyGateMsg(gateRef);
    masterRef.tell(msg, commRef);
  }
}
