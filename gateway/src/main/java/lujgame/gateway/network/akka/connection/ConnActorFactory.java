package lujgame.gateway.network.akka.connection;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.Creator;
import io.netty.channel.ChannelHandlerContext;
import lujgame.core.akka.schedule.ActorScheduler;
import lujgame.gateway.network.akka.connection.logic.ConnInfoGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConnActorFactory {

  @Autowired
  public ConnActorFactory(ActorScheduler actorScheduler, ConnInfoGetter connInfoGetter) {
    _actorScheduler = actorScheduler;
    _connInfoGetter = connInfoGetter;
  }

  public Props props(String connId, ChannelHandlerContext nettyContext, ActorRef acceptRef) {
    ConnActorState state = new ConnActorState(connId, nettyContext, acceptRef);
    Creator<ConnActor> c = () -> new ConnActor(state, _actorScheduler, _connInfoGetter);
    return Props.create(ConnActor.class, c);
  }

  public String getActorName(String connId) {
    return "Conn_" + connId;
  }

  private final ActorScheduler _actorScheduler;
  private final ConnInfoGetter _connInfoGetter;
}
