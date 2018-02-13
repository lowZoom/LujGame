package lujgame.core.akka.common.casev2;

public interface PostStopHandler<C> {

  void postStop(C ctx) throws Exception;
}
