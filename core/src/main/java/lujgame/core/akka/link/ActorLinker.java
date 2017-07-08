package lujgame.core.akka.link;

import akka.actor.Props;
import akka.actor.UntypedActorContext;
import lujgame.core.akka.link.client.LinkClientActorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActorLinker {

  @Autowired
  public ActorLinker(LinkClientActorFactory linkActorFactory) {
    _linkActorFactory = linkActorFactory;
  }

  public void link(UntypedActorContext ctx, String linkUrl) {
    Props props = _linkActorFactory.props(linkUrl);
    ctx.actorOf(props);
  }

  private final LinkClientActorFactory _linkActorFactory;
}
