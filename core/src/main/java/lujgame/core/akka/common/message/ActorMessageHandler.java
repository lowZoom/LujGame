package lujgame.core.akka.common.message;

import akka.event.LoggingAdapter;

public abstract class ActorMessageHandler {

  public abstract Result handleMessage(MessageHandleContext ctx);

  public enum Result {
    /**
     * 表示消息处理已结束，不再往后传递
     */
    FINISH,

    /**
     * 表示该消息还需后续处理，消息往后传递
     */
    CONTINUE,

    /**
     * 表示该消息没被处理，需要继续寻找处理器
     */
    SKIP,
  }

  public static LoggingAdapter log(MessageHandleContext ctx) {
    return ctx.getActorState().getLogger();
  }
}
