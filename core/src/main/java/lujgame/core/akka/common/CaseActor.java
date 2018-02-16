package lujgame.core.akka.common;

import akka.actor.ActorSystem;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorContext;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import lujgame.core.akka.common.message.ActorMessageHandler;
import lujgame.core.akka.common.message.MessageHandleContext;
import lujgame.core.akka.common.message.handlers.DefaultMsgHdl;

@Deprecated
public abstract class CaseActor extends UntypedActor {

  @Override
  public void onReceive(Object msg) throws Exception {
    Impl impl = Impl.SINGLETON;

    if (impl.handleMessage(_state, msg)) {
      return;
    }

    logUnhandled(msg);
  }

  public <T> void addCase(Class<T> msgType, Consumer<T> action) {
    Map<Class<?>, Consumer<?>> actionMap = _state.getActionMap();
    actionMap.put(msgType, action);
  }

  public void prependMessageHandler(ActorMessageHandler handler) {
    LinkedList<ActorMessageHandler> pipeline = _state.getMessagePipeline();
    pipeline.addFirst(handler);
  }

  @SuppressWarnings("ThisEscapedInObjectConstruction")
  protected CaseActor() {
    _state = new CaseActorState(this,
        initLog(),
        initMessagePipeline());
  }

  protected LoggingAdapter log() {
    return _state.getLogger();
  }

  private LoggingAdapter initLog() {
    UntypedActorContext ctx = getContext();
    ActorSystem system = ctx.system();

    return Logging.getLogger(system, this);
  }

  private LinkedList<ActorMessageHandler> initMessagePipeline() {
    LinkedList<ActorMessageHandler> pipeline = new LinkedList<>();

//    pipeline.addLast(new ScheduleMsgHdl());
    pipeline.addLast(new DefaultMsgHdl());

    return pipeline;
  }

  private void logUnhandled(Object msg) {
    log().warning("未处理的消息：{}（{}）", msg, msg.getClass());
    unhandled(msg);
  }

  public enum Impl {
    SINGLETON;

    public boolean handleMessage(CaseActorState state, Object msg) {
      LinkedList<Object> msgQueue = new LinkedList<>();
      msgQueue.addLast(msg);

      List<ActorMessageHandler> msgPipeline = state.getMessagePipeline();
      boolean handled = false;

      while (!msgQueue.isEmpty()) {
        Object msgVar = msgQueue.removeFirst();
        MessageHandleContext ctx = new MessageHandleContext(state, msgVar, msgQueue);

        if (handleOneMsg(ctx, msgPipeline)) {
          handled = true;
        }
      }

      return handled;
    }

    private static boolean handleOneMsg(MessageHandleContext ctx,
        List<ActorMessageHandler> msgPipeline) {
      boolean handled = false;

      for (ActorMessageHandler handler : msgPipeline) {
        ActorMessageHandler.Result result = handler.handleMessage(ctx);
        if (result == ActorMessageHandler.Result.SKIP) {
          continue;
        }

        handled = true;
        if (result == ActorMessageHandler.Result.FINISH) {
          break;
        }
      }

      return handled;
    }
  }

  final CaseActorState _state;
}
