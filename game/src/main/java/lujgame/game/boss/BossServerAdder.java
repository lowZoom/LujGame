package lujgame.game.boss;

import akka.actor.Address;
import akka.actor.UntypedActorContext;
import akka.cluster.Member;
import akka.event.LoggingAdapter;
import org.springframework.stereotype.Component;

@Component
public class BossServerAdder {

  public boolean isGameServer(Member member) {
    return member.roles().contains("server");
  }

  public void addServer(UntypedActorContext ctx, Member member, LoggingAdapter log) {
    Address addr = member.address();

    log.debug("新的节点 -> {}, {}", addr, member.roles());
  }
}
