package lujgame.core.akka.schedule;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import akka.actor.Cancellable;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import lujgame.core.akka.common.CaseActorState;
import lujgame.core.akka.common.message.ActorMessageHandler;
import lujgame.core.akka.common.message.MessageHandleContext;

public class ScheduleMsgHdl extends ActorMessageHandler {

  @Override
  public Result handleMessage(MessageHandleContext ctx) {
    // 打断
    tryCancel(ctx);

    Object rawMsg = ctx.getMessage();
    if (!(rawMsg instanceof ScheduleMsg)) {
      return Result.SKIP;
    }

    // 清理
    tryFinish(ctx, (ScheduleMsg) rawMsg);

    return Result.FINISH;
  }

  /**
   * 通过信号打断原有调度
   */
  private static void tryCancel(MessageHandleContext ctx) {
    CaseActorState actorState = ctx.getActorState();
    Multimap<Class<?>, ScheduleItem> interruptMap = actorState.getInterruptMap();

    // 还没初始化，说明尚未有任何调度需要打断，中止
    if (interruptMap == null) {
      return;
    }

    Object rawMsg = ctx.getMessage();
    Class<?> msgType = rawMsg.getClass();
    Collection<ScheduleItem> items = interruptMap.get(msgType);

    // 未有任何打断注册在该消息类型下，中止
    if (items.isEmpty()) {
      return;
    }

//    log(ctx).debug("打断！！！---> {}", msgType);

    Map<String, ScheduleItem> scheduleMap = actorState.getScheduleMap();
    checkNotNull(scheduleMap, "都可以打断了还没初始化，schedule的时候没有初始化？");

    for (ScheduleItem item : items) {
      Cancellable cancellable = item.getCancellable();
      cancellable.cancel();

      String scheduleId = item.getScheduleId();
      ScheduleItem oldItem = scheduleMap.remove(scheduleId);

      checkState(Objects.equals(item, oldItem),
          "不应该不相等，检查put逻辑：%s <-> %s", item, oldItem);

//      log(ctx).debug("******** {}", item.getScheduleId());
    }

    items.clear();
  }

  /**
   * 正常调度结束，执行清理工作
   */
  private static void tryFinish(MessageHandleContext ctx, ScheduleMsg scheduleMsg) {
    CaseActorState actorState = ctx.getActorState();
    Map<String, ScheduleItem> scheduleMap = actorState.getScheduleMap();
    checkNotNull(scheduleMap, "这里不可能为空，在请求定时后就应该已初始化");

    // 取出对应调度项
    String scheduleId = scheduleMsg.getScheduleId();
    ScheduleItem item = scheduleMap.remove(scheduleId);

    // 调用对应消息处理器
    Object msg = item.getMessage();
    ctx.addExtraMessage(msg);

    // 没有注册过打断消息，中止
    Class<?> interruptType = item.getInterruptType();
    if (interruptType == null) {
      return;
    }

    // 清理打断注册消息
    Multimap<Class<?>, ScheduleItem> interruptMap = actorState.getInterruptMap();
    checkNotNull(interruptMap, "都注册过打断消息了还没初始化，检查打断注册逻辑");

    Collection<ScheduleItem> cancelCol = interruptMap.get(interruptType);
    cancelCol.remove(item);
  }
}
