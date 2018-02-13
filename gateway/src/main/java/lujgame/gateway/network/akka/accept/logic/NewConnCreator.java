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
import lujgame.gateway.network.akka.connection.logic.packet.ConnPacketBuffer;
import lujgame.gateway.network.akka.connection.logic.state.ConnActorState;
import org.springframework.stereotype.Service;

@Service
public class NewConnCreator {

  public ConnectionItem createConnection(NetAcceptActor acceptActor,
      NetAcceptState state, NewConnMsg msg) {
    String connId = useNextId(state);
    ConnPacketBuffer packetBuf = new ConnPacketBuffer();

    ChannelHandlerContext nettyCtx = msg.getNettyContext();
    ActorRef acceptRef = acceptActor.getSelf();

    ConnActorState actorState = new ConnActorState(connId, packetBuf, nettyCtx, acceptRef);
    Props props = _connActorFactory.props(actorState);

    UntypedActorContext ctx = acceptActor.getContext();
    ActorRef connRef = ctx.actorOf(props, getActorName(connId));
    return new ConnectionItem(connId, connRef);
  }

  public void addToMap(NetAcceptState state, ConnectionItem item, LoggingAdapter log) {
    Map<String, ConnectionItem> connMap = state.getConnectionMap();
    String connId = item.getConnId();
    connMap.put(connId, item);

    log.debug("增加新连接，当前连接数量：{}", connMap.size());
  }

  private String useNextId(NetAcceptState state) {
    long connIdVal = state.getNextConnId();
    state.setNextConnId(connIdVal + 1);
    return Long.toString(connIdVal);
  }

  private String getActorName(String connId) {
    return "Conn_" + connId;
  }

  @Inject
  private GateConnActorFactory _connActorFactory;
}
