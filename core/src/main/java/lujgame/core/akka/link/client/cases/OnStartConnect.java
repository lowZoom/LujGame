package lujgame.core.akka.link.client.cases;

import akka.actor.UntypedActor;
import javax.inject.Inject;
import lujgame.core.akka.feature.ActorFeature;
import lujgame.core.akka.feature.FeatureActorNameMaker;
import lujgame.core.akka.link.client.LinkClientActor;
import lujgame.core.akka.link.client.LinkClientActorState;
import lujgame.core.akka.link.client.message.StartConnectMsg;
import org.springframework.stereotype.Service;

@Service
public class OnStartConnect implements LinkClientActor.Case<StartConnectMsg> {

  @Override
  public void onHandle(LinkClientActor.Context ctx) {
    LinkClientActorState actorState = ctx.getActorState();
    UntypedActor reqActor = ctx.getActor();
    actorState.setRequestorRef(reqActor.getSender());

    StartConnectMsg msg = ctx.getMessage(this);
    actorState.setSuccessMsg(msg.getSuccessMsg());

    String serverName = _featureActorNameMaker.makeName(ActorFeature.LINK_SERVER);
    actorState.setServerUrl(String.format("%s/%s", msg.getLinkUrl(), serverName));
  }

  @Inject
  private FeatureActorNameMaker _featureActorNameMaker;
}
