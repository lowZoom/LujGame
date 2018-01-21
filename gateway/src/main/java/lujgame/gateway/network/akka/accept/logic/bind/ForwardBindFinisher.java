package lujgame.gateway.network.akka.accept.logic.bind;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import io.netty.channel.ChannelHandlerContext;
import javax.inject.Inject;
import lujgame.gateway.network.GateOpcode;
import lujgame.gateway.network.akka.accept.logic.ConnKiller;
import lujgame.gateway.network.akka.connection.logic.ConnPacketReceiver;
import lujgame.gateway.network.akka.connection.logic.ConnPacketSender;
import lujgame.gateway.network.akka.connection.logic.state.ConnActorState;
import org.springframework.stereotype.Service;

@Service
public class ForwardBindFinisher {

  /**
   * 在网关ConnActor中完成绑定动作
   *
   * @see ConnPacketReceiver#bindForward
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
    ChannelHandlerContext nettyCtx = state.getNettyContext();
    _connPacketSender.sendPacket(nettyCtx, GateOpcode.BIND, EMPTY_DATA);
  }

  private static final byte[] EMPTY_DATA = new byte[0];

  @Inject
  private ConnKiller _connKiller;

  @Inject
  private ConnPacketSender _connPacketSender;
}
