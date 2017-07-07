package lujgame.core.akka.link.logic;

import akka.actor.Props;
import akka.actor.UntypedActorContext;
import lujgame.core.akka.link.LinkActorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActorLinker {

  @Autowired
  public ActorLinker(LinkActorFactory linkActorFactory) {
    _linkActorFactory = linkActorFactory;
  }

  public void link(UntypedActorContext ctx, String linkUrl) {
    Props props = _linkActorFactory.props(linkUrl);
    ctx.actorOf(props);
  }

  private final LinkActorFactory _linkActorFactory;
}
