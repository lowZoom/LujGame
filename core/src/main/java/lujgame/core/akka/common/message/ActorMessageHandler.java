package lujgame.core.akka.common.message;

public abstract class ActorMessageHandler {

  public abstract Result handleMessage(MessageHandleContext ctx);

  public enum Result {
    FINISH,
    CONTINUE,
    SKIP,
  }
}
