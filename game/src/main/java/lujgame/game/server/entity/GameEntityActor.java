package lujgame.game.server.entity;

import lujgame.core.akka.common.CaseActor;
import lujgame.game.server.entity.logic.NetPacketConsumer;
import lujgame.gateway.network.akka.connection.logic.packet.GateNetPacket;

/**
 * 绑定一条连接
 */
public class GameEntityActor extends CaseActor {

  public GameEntityActor(
      GameEntityActorState state,
      NetPacketConsumer netPacketConsumer) {
    _state = state;
    _netPacketConsumer = netPacketConsumer;

    addCase(GateNetPacket.class, this::onNetPacket);
  }

  /**
   * 处理网关服投递过来的包
   */
  private void onNetPacket(GateNetPacket msg) {
    _netPacketConsumer.consumePacket(_state, getSelf(), msg, log());
  }

  private final GameEntityActorState _state;

  private final NetPacketConsumer _netPacketConsumer;
}
