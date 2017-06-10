package lujgame.gateway.network.akka.accept.logic;

import akka.event.LoggingAdapter;
import java.util.Map;
import lujgame.gateway.network.akka.accept.NetAcceptState;
import org.springframework.stereotype.Component;

@Component
public class ConnKiller {

  public void killConnection(NetAcceptState state, String connId, LoggingAdapter log) {
    Map<String, ConnItem> connMap = state.getConnectionMap();
    connMap.remove(connId);

    log.debug("连接被销毁，当前连接数量：{}", connMap.size());
  }
}
