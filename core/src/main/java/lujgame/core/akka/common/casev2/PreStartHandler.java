package lujgame.core.akka.common.casev2;

public interface PreStartHandler<C> {

  void preStart(C ctx) throws Exception;
}
