package lujgame.gateway.network.akka.connection;

import akka.actor.ActorRef;
import java.net.InetSocketAddress;
import lujgame.core.akka.common.CaseActor;
import lujgame.gateway.network.akka.accept.logic.ConnKiller;
import lujgame.gateway.network.akka.accept.logic.ForwardBinder;
import lujgame.gateway.network.akka.accept.message.BindForwardRsp;
import lujgame.gateway.network.akka.connection.logic.ConnInfoGetter;
import lujgame.gateway.network.akka.connection.logic.ConnPacketReceiver;
import lujgame.gateway.network.akka.connection.logic.ConnPacketSender;
import lujgame.gateway.network.akka.connection.logic.DumbDetector;
import lujgame.gateway.network.akka.connection.logic.state.ConnActorState;
import lujgame.gateway.network.akka.connection.message.Game2GateMsg;
import lujgame.gateway.network.akka.connection.message.Netty2GateMsg;

/**
 * 处理一条连接相关逻辑
 */
public class GateConnActor extends CaseActor {

  public enum Destroy {MSG}

  public enum Dumb {MSG}

  public GateConnActor(
      ConnActorState state,
      ConnPacketReceiver packetReceiver,
      ConnPacketSender packetSender,
      ForwardBinder forwardBinder,
      ConnKiller connKiller,
      DumbDetector dumbDetector,
      ConnInfoGetter connInfoGetter) {
    _state = state;

    _packetReceiver = packetReceiver;
    _packetSender = packetSender;

    _forwardBinder = forwardBinder;
    _connKiller = connKiller;

    _dumbDetector = dumbDetector;
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
    _dumbDetector.startDetect(this);
  }

  @Override
  public void postStop() throws Exception {
//    log().debug("连接被销毁 -> {}", _state.getConnId());
    _state.getNettyContext().close();
  }

  private void registerMessage() {
    addCase(Netty2GateMsg.class, this::onNettyData);

    addCase(BindForwardRsp.class, this::onBindForwardRsp);
    addCase(Game2GateMsg.class, this::onGameData);

    addCase(Destroy.class, this::onDestroy);
    addCase(Dumb.class, this::onDumb);

    //TODO: 定夺这里要怎么写，来开启暂停处理功能
//    PauseMsgHdl.enable(this);
  }

  private void onNettyData(Netty2GateMsg msg) {
    _packetReceiver.receivePacket(_state, msg.getData(), getSelf(), log());
  }

  private void onBindForwardRsp(BindForwardRsp msg) {
    _forwardBinder.finishBind(_state, msg.getForwardRef(), msg.getForwardId(), getSelf(), log());
  }

  private void onGameData(Game2GateMsg msg) {
    _packetSender.sendPacket(_state.getNettyContext(), 233, msg.getData());
  }

  private void onDestroy(@SuppressWarnings("unused") Destroy ignored) {
    _connKiller.requestKill(_state, getSelf());
  }

  private void onDumb(@SuppressWarnings("unused") Dumb ignored) {
    _dumbDetector.destroyDumb(_state, getSelf(), log());
  }

  private final ConnActorState _state;

  private final ConnPacketReceiver _packetReceiver;
  private final ConnPacketSender _packetSender;

  private final ForwardBinder _forwardBinder;
  private final ConnKiller _connKiller;

  private final DumbDetector _dumbDetector;
  private final ConnInfoGetter _connInfoGetter;
}
