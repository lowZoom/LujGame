package lujgame.game.server.entity.logic;

import akka.event.LoggingAdapter;
import lujgame.game.server.entity.GameEntityActorState;
import lujgame.gateway.network.akka.connection.logic.packet.GateNetPacket;
import org.springframework.stereotype.Component;

@Component
public class NetPacketConsumer {

  public void consumePacket(GameEntityActorState state, GateNetPacket packet, LoggingAdapter log) {
    //TODO: 解包并调用对应处理器

    Integer opcode = packet.getOpcode();
    log.debug("游戏服收到包，等待处理 -> {}", opcode);

//    GameNetHandler handler = _netHandlerMap.getNetHandler(opcode);
//    GameNetHandleContext ctx = new GameNetHandleContext();
//
//    handler.onHandle(ctx);
  }
}
