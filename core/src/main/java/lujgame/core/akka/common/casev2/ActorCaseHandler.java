package lujgame.core.akka.common.casev2;

public interface ActorCaseHandler<C, M> {

  void onHandle(C ctx);
}
