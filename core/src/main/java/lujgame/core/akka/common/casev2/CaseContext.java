package lujgame.core.akka.common.casev2;

import akka.event.LoggingAdapter;

public interface CaseContext<S, M> {

  S getActorState();

  M getMessage();

  LoggingAdapter getActorLogger();

  CaseActorV2 getActor();
}
