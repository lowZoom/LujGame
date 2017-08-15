package lujgame.game.server.entity.logic;

import akka.event.LoggingAdapter;
import com.google.common.collect.ImmutableMap;
import lujgame.game.server.entity.GameEntityActorState;
import lujgame.game.server.net.GameNetCodec;
import lujgame.game.server.net.GameNetHandleContext;
import lujgame.game.server.net.GameNetHandler;
import lujgame.game.server.net.NetHandleSuite;
import lujgame.gateway.network.akka.connection.logic.packet.GateNetPacket;
import org.springframework.stereotype.Component;

@Component
public class NetPacketConsumer {

  public void consumePacket(GameEntityActorState state, GateNetPacket packet, LoggingAdapter log) {
    //TODO: 解包并调用对应处理器

    Integer opcode = packet.getOpcode();
    log.debug("游戏服收到包，等待处理 -> {}", opcode);

    ImmutableMap<Integer, NetHandleSuite> suiteMap = state.getHandleSuiteMap();
    NetHandleSuite suite = suiteMap.get(opcode);

    GameNetCodec codec = suite.getNetCodec();
    Object proto = codec.decode(packet.getData());

    GameNetHandler<?> handler = suite.getNetHandler();
    GameNetHandleContext ctx = new GameNetHandleContext(proto);

    handler.onHandle(ctx);
  }
}
