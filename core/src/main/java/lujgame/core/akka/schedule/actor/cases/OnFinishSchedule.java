package lujgame.core.akka.schedule.actor.cases;

import static com.google.common.base.Preconditions.checkNotNull;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import java.util.Map;
import lujgame.core.akka.schedule.actor.ScheduleActor;
import lujgame.core.akka.schedule.actor.message.FinishScheduleMsg;
import lujgame.core.akka.schedule.control.state.ScheduleActorState;
import lujgame.core.akka.schedule.control.state.ScheduleItem;
import org.springframework.stereotype.Service;

/**
 * 定时时间到触发完成消息
 */
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
  }
}
