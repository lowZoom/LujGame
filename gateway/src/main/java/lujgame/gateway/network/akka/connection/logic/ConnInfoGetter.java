package lujgame.gateway.network.akka.connection.logic;

import java.net.InetSocketAddress;
import lujgame.gateway.network.akka.connection.ConnActorState;
import org.springframework.stereotype.Component;

@Component
public class ConnInfoGetter {

  public InetSocketAddress getRemoteAddress(ConnActorState state) {
    return (InetSocketAddress) state.getNettyContext().channel().remoteAddress();
  }
}
