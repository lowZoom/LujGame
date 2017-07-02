package lujgame.gateway.network.akka.accept.message;

import akka.actor.ActorRef;
import java.io.Serializable;

public class BindForwardRsp implements Serializable {

  public BindForwardRsp(ActorRef forwardRef) {
    _forwardRef = forwardRef;
  }



  private final ActorRef _forwardRef;
}
