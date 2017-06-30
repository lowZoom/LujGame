package lujgame.core.akka.message;

public abstract class ActorMessageHandler {

  public abstract Result handleMessage(MessageHandleContext ctx);

  public enum Result {
    FINISH,
    CONTINUE,
    SKIP,
  }
}
