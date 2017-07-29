package lujgame.game.server.entity.logic;

import lujgame.game.server.entity.GameEntityActorState;
import lujgame.gateway.network.akka.connection.logic.packet.GateNetPacket;
import org.springframework.stereotype.Component;

@Component
public class NetPacketConsumer {

  public void consumePacket(GameEntityActorState state, GateNetPacket packet) {
    //TODO: 解包并调用对应处理器

  }
}
