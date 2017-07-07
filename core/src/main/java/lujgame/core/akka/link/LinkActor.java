package lujgame.core.akka.link;

import lujgame.core.akka.common.CaseActor;
import lujgame.core.akka.schedule.ActorScheduler;

/**
 * 让两个actor保持连接
 */
public class LinkActor extends CaseActor {

  public LinkActor(LinkActorState state) {
    _state = state;
  }

  @Override
  public void preStart() throws Exception {
    log().debug("开始尝试连接 -> {}", _state.getLinkUrl());
  }

  enum Connect {MSG}

  private final LinkActorState _state;

  private final ActorScheduler _actorScheduler;
}
