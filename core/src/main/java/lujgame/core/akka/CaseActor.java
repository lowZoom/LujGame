package lujgame.core.akka;

import akka.actor.ActorSystem;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorContext;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import lujgame.core.akka.message.ActorMessageHandler;
import lujgame.core.akka.message.MessageHandleContext;
import lujgame.core.akka.message.handlers.DefaultMsgHdl;

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
        new HashMap<>(32),
        initLog(),
        initMessagePipeline(),
        new LinkedList<>());
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
    pipeline.addFirst(new DefaultMsgHdl());

    return pipeline;
  }

  private void logUnhandled(Object msg) {
    log().warning("未处理的消息：{}（{}）", msg, msg.getClass());
    unhandled(msg);
  }

  public enum Impl {
    SINGLETON;

    public boolean handleMessage(CaseActorState state, Object msg) {
      LinkedList<Object> msgQueue=state.getMessageQueue();
      msgQueue.addLast(msg);

      List<ActorMessageHandler> msgPipeline = state.getMessagePipeline();
      boolean handled = false;

      while (!msgQueue.isEmpty()) {
        Object msgVar = msgQueue.removeFirst();
        MessageHandleContext ctx = new MessageHandleContext(state, msgVar);

        if (handleMsgIter(ctx, msgPipeline)) {
          handled = true;
        }
      }

      return handled;
    }

    static boolean handleMsgIter(MessageHandleContext ctx, List<ActorMessageHandler> msgPipeline) {
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

  private final CaseActorState _state;
}
