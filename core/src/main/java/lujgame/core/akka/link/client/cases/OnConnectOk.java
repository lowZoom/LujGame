package lujgame.core.akka.link.client.cases;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import lujgame.core.akka.feature.ActorFeature;
import lujgame.core.akka.feature.FeatureDispatchMsg;
import lujgame.core.akka.link.client.LinkClientActor;
import lujgame.core.akka.link.client.LinkClientActorState;
import lujgame.core.akka.link.message.LinkConnect;
import lujgame.core.akka.schedule.actor.message.CancelScheduleMsg;
import org.springframework.stereotype.Service;

/**
 * 成功连接上远程监听节点
 */
@Service
public class OnConnectOk implements LinkClientActor.Case<LinkConnect.Ok> {

  @Override
  public void onHandle(LinkClientActor.Context ctx) {
    LinkClientActorState actorState = ctx.getActorState();
    UntypedActor actor = ctx.getActor();

    finishConnect(actorState, actor.getSender());
    cancelSchedule(actor.getSelf(), OnTryConnect.ScheduleId.TRY);
  }

  private void finishConnect(LinkClientActorState state, ActorRef listenRef) {
//    log().debug("link连接成功！！！！！！！！！！！！！！！！！！！");

    ActorRef reqRef = state.getRequestorRef();
    Enum<?> successMsg = state.getSuccessMsg();
    reqRef.tell(successMsg, listenRef);
  }

  private void cancelSchedule(ActorRef targetRef, String scheduleId) {
    CancelScheduleMsg cancelMsg = new CancelScheduleMsg(scheduleId);
    FeatureDispatchMsg featureMsg = new FeatureDispatchMsg(ActorFeature.SCHEDULE, cancelMsg);
    targetRef.tell(featureMsg, ActorRef.noSender());
  }
}
