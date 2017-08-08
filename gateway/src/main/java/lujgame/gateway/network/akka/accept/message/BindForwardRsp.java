package lujgame.gateway.network.akka.accept.message;

import akka.actor.ActorRef;
import java.io.Serializable;

public class BindForwardRsp implements Serializable {

  public BindForwardRsp(String forwardId, ActorRef forwardRef) {
    _forwardId = forwardId;
    _forwardRef = forwardRef;
  }

  public String getForwardId() {
    return _forwardId;
  }

  /**
   * @return null表示请求的forwardId无效
   */
  public ActorRef getForwardRef() {
    return _forwardRef;
  }

  private final String _forwardId;

  private final ActorRef _forwardRef;
}
