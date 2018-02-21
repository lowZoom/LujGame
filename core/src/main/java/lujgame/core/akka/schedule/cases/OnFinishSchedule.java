package lujgame.core.akka.schedule.cases;

import static com.google.common.base.Preconditions.checkNotNull;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Map;
import lujgame.core.akka.schedule.ScheduleActor;
import lujgame.core.akka.schedule.ScheduleActorState;
import lujgame.core.akka.schedule.ScheduleItem;
import lujgame.core.akka.schedule.message.FinishScheduleMsg;
import org.springframework.stereotype.Service;

@Service
public class OnFinishSchedule implements ScheduleActor.Case<FinishScheduleMsg> {

  @Override
  public void onHandle(ScheduleActor.Context ctx) {
    ScheduleActorState actorState = ctx.getActorState();
    FinishScheduleMsg msg = ctx.getMessage(this);

//    Object rawMsg = ctx.getMessage();
//    if (!(rawMsg instanceof ScheduleMsg)) {
//      return Result.SKIP;
//    }

    // 清理
    UntypedActor actor = ctx.getActor();
    tryFinish(actorState, msg, actor.getSender());

//    return Result.FINISH;
  }

  /**
   * 正常调度结束，执行清理工作
   */
  private void tryFinish(ScheduleActorState actorState,
      FinishScheduleMsg finishMsg, ActorRef sender) {
    Map<String, ScheduleItem> scheduleMap = actorState.getScheduleMap();
    checkNotNull(scheduleMap);

    // 取出对应调度项
    String scheduleId = finishMsg.getScheduleId();
    ScheduleItem item = scheduleMap.remove(scheduleId);
    checkNotNull(item, scheduleId);

    // 调用对应消息处理器
    Object msg = item.getMessage();
    item.getReceiver().tell(msg, sender);

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
