package lujgame.core.akka.common.casev2;

import akka.actor.UntypedActor;
import akka.event.LoggingAdapter;

public abstract class CaseActorContext<S> {

  @SuppressWarnings({"unchecked", "unused"})
  public <M> M getMessage(ActorCaseHandler<?, M> handler) {
    return (M) _message;
  }

  public S getActorState() {
    return _actorState;
  }

  public LoggingAdapter getActorLogger() {
    return _actorLogger;
  }

  public UntypedActor getActor() {
    return _actor;
  }

  S _actorState;

  Object _message;

  LoggingAdapter _actorLogger;

  UntypedActor _actor;
}
