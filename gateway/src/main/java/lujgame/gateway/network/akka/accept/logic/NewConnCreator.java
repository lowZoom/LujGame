package lujgame.gateway.network.akka.accept.logic;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActorContext;
import akka.event.LoggingAdapter;
import io.netty.channel.ChannelHandlerContext;
import java.util.Map;
import javax.inject.Inject;
import lujgame.gateway.network.akka.accept.NetAcceptActor;
import lujgame.gateway.network.akka.accept.NetAcceptState;
import lujgame.gateway.network.akka.accept.message.NewConnMsg;
import lujgame.gateway.network.akka.connection.GateConnActorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class NewConnCreator {

  public ConnectionItem createConnection(NetAcceptActor acceptActor,
      NetAcceptState state, NewConnMsg msg) {
    String connId = Long.toString(state.getNextConnId());
    state.setNextConnId(state.getNextConnId() + 1);

    GateConnActorFactory connFactory = _connActorFactory;
    ChannelHandlerContext nettyCtx = msg.getNettyContext();
    ActorRef acceptRef = acceptActor.getSelf();
    Props props = connFactory.props(connId, nettyCtx, acceptRef);

    UntypedActorContext ctx = acceptActor.getContext();
    String name = connFactory.getActorName(connId);
    ActorRef connRef = ctx.actorOf(props, name);
    return new ConnectionItem(connId, connRef);
  }

  public void addToMap(NetAcceptState state, ConnectionItem item, LoggingAdapter log) {
    Map<String, ConnectionItem> connMap = state.getConnectionMap();
    String connId = item.getConnId();
    connMap.put(connId, item);

    log.debug("增加新连接，当前连接数量：{}", connMap.size());
  }

  @Inject
  private GateConnActorFactory _connActorFactory;
}
