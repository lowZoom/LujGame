package lujgame.game.master.gate.cases;

import akka.event.LoggingAdapter;
import lujgame.game.master.gate.CommGateActor;
import org.springframework.stereotype.Service;

@Service
public class OnGateLink implements CommGateActor.Case<CommGateActor.NewGateConnect> {

  @Override
  public void onHandle(CommGateActor.Context ctx) {
    LoggingAdapter log = ctx.getActorLogger();
    log.debug("检测到新的网关连接！！@！@");
  }
}
