package lujgame.core.akka.common.message.handlers;

import java.util.LinkedList;
import lujgame.core.akka.common.CaseActor;
import lujgame.core.akka.common.message.ActorMessageHandler;
import lujgame.core.akka.common.message.MessageHandleContext;

public class PauseMsgHdl extends ActorMessageHandler {

  public enum Resume {MSG}

  public static void enable(CaseActor actor) {
    actor.prependMessageHandler(new PauseMsgHdl());
  }

  public PauseMsgHdl() {
    _messageBuf = new LinkedList<>();
  }

  @Override
  public Result handleMessage(MessageHandleContext ctx) {
    Object msg = ctx.getMessage();

    // 恢复处理缓存的消息，并移除本处理器
    if (msg instanceof Resume) {
      flushMessage(ctx);
      ctx.removeHandler(this);
      return Result.CONTINUE;
    }

    log(ctx).debug("Pause!!!!!!!!!!!!!! -> {} -> {}", ctx, msg.getClass());

    // 将暂停处理的消息缓存起来，以便恢复时处理
    _messageBuf.addLast(msg);

    // 阻断后续处理，达到暂停消息处理的目的
    return Result.FINISH;
  }

  private void flushMessage(MessageHandleContext ctx) {
    LinkedList<Object> msgBuf = _messageBuf;
    msgBuf.forEach(ctx::addExtraMessage);
    msgBuf.clear();
  }

  private final LinkedList<Object> _messageBuf;
}
