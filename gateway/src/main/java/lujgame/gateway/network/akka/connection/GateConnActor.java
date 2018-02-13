package lujgame.gateway.network.akka.connection;

import lujgame.core.akka.common.casev2.ActorCaseHandler;
import lujgame.core.akka.common.casev2.CaseActorContext;
import lujgame.core.akka.common.casev2.CaseActorV2;
import lujgame.core.akka.common.casev2.PostStopHandler;
import lujgame.core.akka.common.casev2.PreStartHandler;
import lujgame.gateway.network.akka.connection.logic.state.ConnActorState;

/**
 * 处理一条连接相关逻辑，与Netty直接通讯
 */
public class GateConnActor extends CaseActorV2<ConnActorState> {

  public static class Context extends CaseActorContext<ConnActorState> {

  }

  public interface Case<M> extends ActorCaseHandler<Context, M> {

  }

  public interface PreStart extends PreStartHandler<Context> {

  }

  public interface PostStop extends PostStopHandler<Context> {

  }

  public enum Destroy {MSG}

  public enum Dumb {MSG}
}
