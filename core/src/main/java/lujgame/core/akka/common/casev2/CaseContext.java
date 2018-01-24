package lujgame.core.akka.common.casev2;

import akka.event.LoggingAdapter;

public abstract class CaseContext<S> {

  public <M> M getMessage(ActorCaseHandler<?, M> handler) {
    return (M) _message;
  }

  public S getActorState() {
    return _actorState;
  }

  public LoggingAdapter getActorLogger() {
    return _actorLogger;
  }

  public CaseActorV2 getActor() {
    return _actor;
  }

   S _actorState;

   Object _message;

   LoggingAdapter _actorLogger;

   CaseActorV2 _actor;
}
