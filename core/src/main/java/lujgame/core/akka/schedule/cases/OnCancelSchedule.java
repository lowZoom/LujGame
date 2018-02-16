package lujgame.core.akka.schedule.cases;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import akka.actor.Cancellable;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import lujgame.core.akka.schedule.ScheduleActor;
import lujgame.core.akka.schedule.ScheduleActorState;
import lujgame.core.akka.schedule.ScheduleItem;
import lujgame.core.akka.schedule.message.CancelScheduleMsg;
import org.springframework.stereotype.Service;

@Service
public class OnCancelSchedule implements ScheduleActor.Case<CancelScheduleMsg> {

  @Override
  public void onHandle(ScheduleActor.Context ctx) {
    ScheduleActorState actorState = ctx.getActorState();

    // 打断
    tryCancel(actorState);
  }

  /**
   * 通过信号打断原有调度
   */
  private static void tryCancel(ScheduleActorState actorState) {
    Multimap<Class<?>, ScheduleItem> interruptMap = actorState.getInterruptMap();

    // 还没初始化，说明尚未有任何调度需要打断，中止
    if (interruptMap == null) {
      return;
    }

    Object rawMsg = null;// ctx.getMessage();
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
}
