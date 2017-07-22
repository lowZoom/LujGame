package lujgame.game.gate;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import java.util.Set;
import lujgame.core.akka.link.message.LinkConnect;
import lujgame.game.master.message.ReplyGateMsg;
import org.springframework.stereotype.Component;

@Component
public class GameGateAdder {

  public void addGate(CommGateActorState state,
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
