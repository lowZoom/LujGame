package lujgame.gateway.network.akka.connection;

import akka.actor.ActorRef;
import java.net.InetSocketAddress;
import lujgame.core.akka.common.CaseActor;
import lujgame.gateway.network.akka.accept.logic.ConnKiller;
import lujgame.gateway.network.akka.accept.logic.bind.ForwardBindFinisher;
import lujgame.gateway.network.akka.accept.message.BindForwardRsp;
import lujgame.gateway.network.akka.connection.logic.ConnPacketReceiver;
import lujgame.gateway.network.akka.connection.logic.ConnPacketSender;
import lujgame.gateway.network.akka.connection.logic.DumbDetector;
import lujgame.gateway.network.akka.connection.logic.GateConnActorDependency;
import lujgame.gateway.network.akka.connection.logic.state.ConnActorState;
import lujgame.gateway.network.akka.connection.message.Game2GateMsg;
import lujgame.gateway.network.akka.connection.message.Netty2GateMsg;

/**
 * 处理一条连接相关逻辑，与Netty直接通讯
 */
public class GateConnActor extends CaseActor {

  public enum Destroy {MSG}

  public enum Dumb {MSG}

  public GateConnActor(ConnActorState state, GateConnActorDependency dependency) {
    _state = state;
    _dependency = dependency;

    registerMessage();
  }

  @Override
  public void preStart() throws Exception {
    ConnActorState state = _state;
    GateConnActorDependency d = _dependency;

    InetSocketAddress remoteAddr = d.getConnInfoGetter().getRemoteAddress(state);
    log().info("新连接 -> {}", remoteAddr);

    ActorRef self = getSelf();
    d.getPacketReceiver().updateNettyHandler(state, self);

    // 启动空连接检测
    d.getDumbDetector().startDetect(this);
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
    ConnPacketReceiver r = _dependency.getPacketReceiver();
    r.receivePacket(_state, msg.getData(), getSelf(), log());
  }

  private void onBindForwardRsp(BindForwardRsp msg) {
    ForwardBindFinisher f = _dependency.getForwardBindFinisher();
    f.finishBind(_state, msg.getForwardRef(), msg.getForwardId(), getSelf(), log());
  }

  private void onGameData(Game2GateMsg msg) {
    ConnPacketSender s = _dependency.getPacketSender();
    s.sendPacket(_state.getNettyContext(), 233, msg.getData());
  }

  private void onDestroy(@SuppressWarnings("unused") Destroy ignored) {
    ConnKiller k = _dependency.getConnKiller();
    k.requestKill(_state, getSelf());
  }

  private void onDumb(@SuppressWarnings("unused") Dumb ignored) {
    DumbDetector d = _dependency.getDumbDetector();
    d.destroyDumb(_state, getSelf(), log());
  }

  private final ConnActorState _state;

  private final GateConnActorDependency _dependency;
}
