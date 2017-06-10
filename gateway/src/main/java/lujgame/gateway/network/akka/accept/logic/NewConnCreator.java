package lujgame.gateway.network.akka.accept.logic;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActorContext;
import akka.event.LoggingAdapter;
import io.netty.channel.ChannelHandlerContext;
import java.util.Map;
import lujgame.gateway.network.akka.accept.NetAcceptActor;
import lujgame.gateway.network.akka.accept.NetAcceptState;
import lujgame.gateway.network.akka.accept.message.NewConnMsg;
import lujgame.gateway.network.akka.connection.ConnActorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewConnCreator {

  @Autowired
  public NewConnCreator(ConnActorFactory connActorFactory) {
    _connActorFactory = connActorFactory;
  }

  public ConnItem createConn(NetAcceptActor acceptActor, NewConnMsg msg) {
    UntypedActorContext ctx = acceptActor.getContext();

    ConnActorFactory connFactory = _connActorFactory;
    String connId = msg.getConnId();
    ChannelHandlerContext nettyCtx = msg.getNettyContext();
    ActorRef acceptRef = acceptActor.getSelf();
    Props props = connFactory.props(connId, nettyCtx, acceptRef);

    String name = connFactory.getActorName(connId);
    ActorRef connRef = ctx.actorOf(props, name);
    return new ConnItem(connId, connRef);
  }

  public void addToMap(NetAcceptState state, ConnItem item, LoggingAdapter log) {
    Map<String, ConnItem> connMap = state.getConnectionMap();
    String connId = item.getConnId();
    connMap.put(connId, item);

    log.debug("增加新连接，当前连接数量：{}", connMap.size());
  }

  private final ConnActorFactory _connActorFactory;
}
