package lujgame.gateway.network.akka.accept.logic.bind;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import javax.inject.Inject;
import lujgame.gateway.network.akka.accept.logic.ConnKiller;
import lujgame.gateway.network.akka.connection.logic.state.ConnActorState;
import org.springframework.stereotype.Service;

@Service
public class ForwardBindFinisher {

  /**
   * 在网关ConnActor中完成绑定动作
   */
  public void finishBind(ConnActorState state, ActorRef forwardRef,
      String forwardId, ActorRef connRef, LoggingAdapter log) {
    if (forwardRef == null) {
      log.warning("[非法]无效的转发节点绑定 -> {}", forwardId);
      log.warning("连接即将被销毁 -> {}", state.getConnId());

      _connKiller.requestKill(state, connRef);
      return;
    }

    state.setForwardRef(forwardRef);

    //TODO: 回复一个空包给客户端表示成功，包头与游戏逻辑包格式一致

  }

  @Inject
  private ConnKiller _connKiller;
}
