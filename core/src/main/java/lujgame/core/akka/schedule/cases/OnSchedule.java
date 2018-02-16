package lujgame.core.akka.schedule.cases;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Map;
import lujgame.core.akka.schedule.ScheduleActor;
import lujgame.core.akka.schedule.ScheduleActorState;
import lujgame.core.akka.schedule.ScheduleItem;
import lujgame.core.akka.schedule.ScheduleMsg;
import org.springframework.stereotype.Service;

@Service
public class OnSchedule implements ScheduleActor.Case<ScheduleMsg> {

  @Override
  public void onHandle(ScheduleActor.Context ctx) {
    ScheduleActorState actorState = ctx.getActorState();
    ScheduleMsg msg = ctx.getMessage(this);

//    Object rawMsg = ctx.getMessage();
//    if (!(rawMsg instanceof ScheduleMsg)) {
//      return Result.SKIP;
//    }

    // 清理
    tryFinish(actorState, msg);

//    return Result.FINISH;
  }

  /**
   * 正常调度结束，执行清理工作
   */
  private static void tryFinish(ScheduleActorState actorState, ScheduleMsg scheduleMsg) {
    Map<String, ScheduleItem> scheduleMap = actorState.getScheduleMap();
    checkNotNull(scheduleMap, "这里不可能为空，在请求定时后就应该已初始化");

    // 取出对应调度项
    String scheduleId = scheduleMsg.getScheduleId();
    ScheduleItem item = scheduleMap.remove(scheduleId);

    // 调用对应消息处理器
    Object msg = item.getMessage();
//    ctx.addExtraMessage(msg);

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
