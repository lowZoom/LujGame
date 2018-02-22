package lujgame.core.akka.common.casev2;

public class TestActor {

  public static class Context extends CaseActorContext<Object> {

  }

  public interface Case<M> extends ActorCaseHandler<Context, M> {

  }
}
