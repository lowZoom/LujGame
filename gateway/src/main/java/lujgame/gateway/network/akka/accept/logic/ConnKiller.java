package lujgame.gateway.network.akka.accept.logic;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import java.util.Map;
import lujgame.gateway.network.akka.accept.NetAcceptState;
import org.springframework.stereotype.Component;

@Component
public class ConnKiller {

  public void killConnection(NetAcceptState state, String connId, LoggingAdapter log) {
    Map<String, ConnectionItem> connMap = state.getConnectionMap();
    ConnectionItem connectionItem = connMap.remove(connId);
    if (connectionItem == null) {
      log.warning("异常的销毁请求，连接id -> {}", connId);
      return;
    }

    ActorRef connRef = connectionItem.getConnRef();

    log.debug("连接被销毁，当前连接数量：{}", connMap.size());
  }
}
