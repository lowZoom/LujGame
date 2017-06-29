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
import lujgame.gateway.network.akka.connection.message.ConnDataMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewConnCreator {

  @Autowired
  public NewConnCreator(ConnActorFactory connActorFactory) {
    _connActorFactory = connActorFactory;
  }

  public ConnectionItem createConnection(NetAcceptActor acceptActor, NewConnMsg msg) {
    UntypedActorContext ctx = acceptActor.getContext();

    ConnActorFactory connFactory = _connActorFactory;
    String connId = msg.getConnId();
    ChannelHandlerContext nettyCtx = msg.getNettyContext();
    ActorRef acceptRef = acceptActor.getSelf();
    Props props = connFactory.props(connId, nettyCtx, acceptRef);

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

  public void forwardData(NetAcceptState state, ActorRef acceptRef, ConnDataMsg msg) {
    Map<String, ConnectionItem> connMap = state.getConnectionMap();
    String connId = msg.getConnId();
    ConnectionItem conn = connMap.get(connId);

    ActorRef connRef = conn.getConnRef();
    connRef.tell(msg, acceptRef);
  }

  /**
   * 连接刚创建(create)出来的时候，对发去的消息是暂停处理的（为确保正确的数据包顺序）
   * 该方法将启动连接的消息处理
   */
  public void startConnection(NetAcceptState state, String connId) {
    Map<String, ConnectionItem> connMap = state.getConnectionMap();
    ConnectionItem connItem = connMap.get(connId);

    //TODO: resume
  }

  private final ConnActorFactory _connActorFactory;
}
