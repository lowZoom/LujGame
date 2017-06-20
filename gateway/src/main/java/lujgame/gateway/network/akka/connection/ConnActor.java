package lujgame.gateway.network.akka.connection;

import akka.actor.ActorRef;
import io.netty.channel.ChannelHandlerContext;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import lujgame.core.akka.CaseActor;
import lujgame.core.akka.schedule.ActorScheduler;
import lujgame.gateway.network.akka.accept.message.KillConnMsg;
import lujgame.gateway.network.akka.connection.logic.ConnInfoGetter;

/**
 * 处理一条连接相关逻辑
 */
public class ConnActor extends CaseActor {

  public enum Dead {MSG}

  enum Dumb {MSG}

  public ConnActor(
      ConnActorState state,
      ActorScheduler actorScheduler,
      ConnInfoGetter connInfoGetter) {
    _state = state;

    _actorScheduler = actorScheduler;
    _connInfoGetter = connInfoGetter;

    registerMessage();
  }

  @Override
  public void preStart() throws Exception {
    InetSocketAddress remoteAddr = _connInfoGetter.getRemoteAddress(_state);
    log().info("新连接 -> {}", remoteAddr);

    _actorScheduler.schedule(this, 3, TimeUnit.SECONDS, Dumb.MSG);
  }

  private void registerMessage() {
    addCase(Dumb.class, this::onDumb);
  }

  private void onDumb(Dumb ignored) {
    ConnActorState state = _state;
    InetSocketAddress remoteAddr = _connInfoGetter.getRemoteAddress(state);
    log().info("新连接 -> {}", remoteAddr);
    log().warning("检测到空连接，即将销毁 -> {}", remoteAddr);

    ActorRef acceptRef = state.getAcceptRef();
    KillConnMsg msg = new KillConnMsg(state.getConnId());
    ActorRef connRef = getSelf();
    acceptRef.tell(msg, connRef);

    //TODO: 实际关闭网络连接
    ChannelHandlerContext nettyCtx = state.getNettyContext();
    nettyCtx.close();
  }

  private final ConnActorState _state;

  private final ActorScheduler _actorScheduler;
  private final ConnInfoGetter _connInfoGetter;
}