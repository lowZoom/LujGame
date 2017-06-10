package lujgame.gateway.network.akka.connection;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.Creator;
import java.net.InetSocketAddress;
import lujgame.core.akka.schedule.ActorScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConnActorFactory {

  @Autowired
  public ConnActorFactory(ActorScheduler actorScheduler) {
    _actorScheduler = actorScheduler;
  }

  public Props props(String connId, InetSocketAddress remoteAddr, ActorRef acceptRef) {
    ConnActorState state = new ConnActorState(connId, remoteAddr, acceptRef);
    Creator<ConnActor> c = () -> new ConnActor(state, _actorScheduler);
    return Props.create(ConnActor.class, c);
  }

  public String getActorName(String connId) {
    return "Conn_" + connId;
  }

  private final ActorScheduler _actorScheduler;
}
