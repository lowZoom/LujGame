package lujgame.gateway.network.akka.connection;

import akka.actor.ActorRef;
import java.net.InetSocketAddress;
import lujgame.core.akka.CaseActor;
import lujgame.core.akka.schedule.ActorScheduler;
import lujgame.gateway.network.akka.accept.logic.ConnKiller;
import lujgame.gateway.network.akka.accept.logic.ForwardBinder;
import lujgame.gateway.network.akka.accept.message.BindForwardRsp;
import lujgame.gateway.network.akka.connection.logic.ConnInfoGetter;
import lujgame.gateway.network.akka.connection.logic.ConnPacketReceiver;
import lujgame.gateway.network.akka.connection.logic.state.ConnActorState;
import lujgame.gateway.network.akka.connection.message.ConnDataMsg;

/**
 * 处理一条连接相关逻辑
 */
public class ConnActor extends CaseActor {

  public enum Destroy {MSG}

  enum Dumb {MSG}

  public ConnActor(
      ConnActorState state,
      ActorScheduler actorScheduler,
      ConnPacketReceiver packetReceiver,
      ForwardBinder forwardBinder,
      ConnKiller connKiller,
      ConnInfoGetter connInfoGetter) {
    _state = state;

    _actorScheduler = actorScheduler;
    _packetReceiver = packetReceiver;

    _forwardBinder = forwardBinder;
    _connKiller = connKiller;

    _connInfoGetter = connInfoGetter;

    registerMessage();
  }

  @Override
  public void preStart() throws Exception {
    ConnActorState state = _state;
    InetSocketAddress remoteAddr = _connInfoGetter.getRemoteAddress(state);
    log().info("新连接 -> {}", remoteAddr);

    ActorRef self = getSelf();
    _packetReceiver.updateNettyHandler(state, self);

    // 启动空连接检测
//    _actorScheduler.schedule(this, 3, TimeUnit.SECONDS, Dumb.MSG);
  }

  @Override
  public void postStop() throws Exception {
    log().debug("连接被销毁 -> {}", _state.getConnId());
    _state.getNettyContext().close();
  }

  private void registerMessage() {
    addCase(ConnDataMsg.class, this::onConnData);
    addCase(BindForwardRsp.class, this::onBindForwardRsp);

    addCase(Destroy.class, this::onDestroy);
    addCase(Dumb.class, this::onDumb);

    //TODO: 定夺这里要怎么写，来开启暂停处理功能
//    PauseMsgHdl.enable(this);
  }

  private void onConnData(ConnDataMsg msg) {
    _packetReceiver.receivePacket(_state, msg.getData(), getSelf(), log());
  }

  private void onBindForwardRsp(BindForwardRsp msg) {
    _forwardBinder.finishBind(_state, msg.getForwardRef(), msg.getForwardId(), getSelf(), log());
  }

  @SuppressWarnings("unused")
  private void onDestroy(Destroy ignored) {
    _connKiller.requestKill(_state, getSelf());
  }

  @SuppressWarnings("unused")
  private void onDumb(Dumb ignored) {
    InetSocketAddress remoteAddr = _connInfoGetter.getRemoteAddress(_state);
    log().warning("[非法]检测到空连接，即将销毁 -> {}", remoteAddr);

    ActorRef self = getSelf();
    self.tell(Destroy.MSG, self);
  }

  private final ConnActorState _state;

  private final ActorScheduler _actorScheduler;
  private final ConnPacketReceiver _packetReceiver;

  private final ForwardBinder _forwardBinder;
  private final ConnKiller _connKiller;

  private final ConnInfoGetter _connInfoGetter;
}
