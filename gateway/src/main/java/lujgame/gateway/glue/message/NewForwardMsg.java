package lujgame.gateway.glue.message;

import akka.actor.ActorRef;
import java.io.Serializable;

/**
 * 有新转发节点时的通知消息
 */
public class NewForwardMsg implements Serializable {

  public NewForwardMsg(String forwardId, ActorRef forwardRef) {
    _forwardId = forwardId;
    _forwardRef = forwardRef;
  }

  public String getForwardId() {
    return _forwardId;
  }

  public ActorRef getForwardRef() {
    return _forwardRef;
  }

  private final String _forwardId;

  private final ActorRef _forwardRef;
}
