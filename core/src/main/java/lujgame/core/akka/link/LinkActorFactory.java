package lujgame.core.akka.link;

import akka.actor.Props;
import akka.japi.Creator;
import org.springframework.stereotype.Component;

@Component
public class LinkActorFactory {

  public Props props(String linkUrl) {
    LinkActorState state = new LinkActorState(linkUrl);

    Creator<LinkActor> c = () -> {
      return new LinkActor(state);
    };
    return Props.create(LinkActor.class, c);
  }
}
