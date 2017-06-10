package lujgame.gateway.network.akka.connection;

import akka.actor.Props;
import akka.japi.Creator;
import java.net.InetSocketAddress;
import org.springframework.stereotype.Component;

@Component
public class ConnActorFactory {

  public Props props(String connId, InetSocketAddress remoteAddr) {
    ConnActorState state = new ConnActorState(connId, remoteAddr);
    Creator<ConnActor> c = () -> new ConnActor(state);
    return Props.create(ConnActor.class, c);
  }

  public String getActorName(String connId) {
    return "Conn_" + connId;
  }
}
