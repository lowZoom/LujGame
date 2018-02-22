package lujgame.core.akka.link;

import akka.actor.ActorRef;
import lujgame.core.akka.feature.ActorFeature;
import lujgame.core.akka.feature.FeatureDispatchMsg;
import lujgame.core.akka.link.client.LinkClientActor;
import lujgame.core.akka.link.client.message.StartConnectMsg;
import lujgame.core.akka.link.server.message.StartListenMsg;
import org.springframework.stereotype.Service;

/**
 * 负责链接两个Actor，可以日后持续通讯
 */
@Service
public class ActorLinker {

  /**
   * @see LinkClientActor
   */
  public void link(String linkUrl, ActorRef requestor, Enum<?> okMsg) {
    StartConnectMsg connectMsg = new StartConnectMsg(linkUrl, okMsg);
    FeatureDispatchMsg featureMsg = new FeatureDispatchMsg(ActorFeature.LINK_CLIENT, connectMsg);
    requestor.tell(featureMsg, requestor);
  }

  public void linkListen(ActorRef listener, Enum<?> newMsg) {
    StartListenMsg listenMsg = new StartListenMsg(newMsg);
    FeatureDispatchMsg featureMsg = new FeatureDispatchMsg(ActorFeature.LINK_SERVER, listenMsg);
    listener.tell(featureMsg, listener);
  }
}
