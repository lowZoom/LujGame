package lujgame.gateway.network.akka.accept.logic;

import akka.actor.ActorRef;
import akka.actor.UntypedActorContext;
import akka.event.LoggingAdapter;
import io.netty.channel.ChannelHandlerContext;
import java.util.Map;
import lujgame.gateway.network.akka.accept.NetAcceptState;
import lujgame.gateway.network.akka.accept.message.KillConnMsg;
import lujgame.gateway.network.akka.connection.logic.state.ConnActorState;
import org.springframework.stereotype.Component;

@Component
public class ConnKiller {

  public void requestKill(ConnActorState state, ActorRef connRef) {
    ActorRef acceptRef = state.getAcceptRef();
    KillConnMsg msg = new KillConnMsg(state.getConnId());
    acceptRef.tell(msg, connRef);

    // 通知netty关闭网络连接
    ChannelHandlerContext nettyCtx = state.getNettyContext();
    nettyCtx.close();
  }

  public void killConnection(NetAcceptState state, String connId,
      UntypedActorContext ctx, LoggingAdapter log) {
    Map<String, ConnectionItem> connMap = state.getConnectionMap();
    ConnectionItem connectionItem = connMap.remove(connId);

    if (connectionItem == null) {
      log.warning("异常的销毁请求，连接id -> {}", connId);
      return;
    }

    ActorRef connRef = connectionItem.getConnRef();
    ctx.stop(connRef);

    log.debug("连接被销毁，当前连接数量：{}", connMap.size());
  }
}
