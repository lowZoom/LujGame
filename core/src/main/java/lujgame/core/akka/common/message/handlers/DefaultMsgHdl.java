package lujgame.core.akka.common.message.handlers;

import java.util.Map;
import java.util.function.Consumer;
import lujgame.core.akka.common.CaseActorState;
import lujgame.core.akka.common.message.ActorMessageHandler;
import lujgame.core.akka.common.message.MessageHandleContext;

public class DefaultMsgHdl extends ActorMessageHandler {

  @Override
  public Result handleMessage(MessageHandleContext ctx) {
    Object msg = ctx.getMessage();
    Class<?> msgType = msg.getClass();
//      logDebug("消息类型：" + msgType);

    Consumer<Object> action = getMessageAction(ctx, msgType);
    if (action == null) {
      return Result.SKIP;
    }

    action.accept(msg);
    return Result.CONTINUE;
  }

  @SuppressWarnings("unchecked")
  private static Consumer<Object> getMessageAction(MessageHandleContext ctx, Class<?> msgType) {
    CaseActorState state = ctx.getActorState();
    Map<Class<?>, Consumer<?>> actionMap = state.getActionMap();
    return (Consumer<Object>) actionMap.get(msgType);
  }
}
