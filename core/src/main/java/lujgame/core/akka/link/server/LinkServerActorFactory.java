package lujgame.core.akka.link.server;

import akka.actor.Props;
import akka.japi.Creator;
import org.springframework.stereotype.Component;

@Component
public class LinkServerActorFactory {

  public Props props() {
    Creator<LinkServerActor> c = () -> new LinkServerActor();

    return Props.create(LinkServerActor.class, c);
  }
}
