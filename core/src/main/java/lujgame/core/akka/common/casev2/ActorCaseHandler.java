package lujgame.core.akka.common.casev2;

public interface ActorCaseHandler<T> {

  void onHandle(T ctx);
}
