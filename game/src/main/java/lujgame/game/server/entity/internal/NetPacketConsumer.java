package lujgame.game.server.entity.internal;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import com.google.common.collect.ImmutableMap;
import lujgame.game.server.entity.GameEntityActorState;
import lujgame.game.server.net.handle.GameNetHandler;
import lujgame.game.server.net.handle.NetHandleContext;
import lujgame.game.server.net.handle.NetHandleSuite;
import lujgame.game.server.net.internal.DbCmdInvoker;
import lujgame.game.server.net.packet.NetPacketCodec;
import lujgame.game.server.type.Jstr0;
import lujgame.game.server.type.Z1;
import lujgame.gateway.network.akka.connection.message.Gate2GameMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NetPacketConsumer {

  @Autowired
  public NetPacketConsumer(
      Z1 typeInternal,
      Jstr0 strInternal,
      DbCmdInvoker dbCmdInvoker) {
    _typeInternal = typeInternal;
    _strInternal = strInternal;

    _dbCmdInvoker = dbCmdInvoker;
  }

  public void consumePacket(GameEntityActorState state, ActorRef entityRef,
      Gate2GameMsg packet, LoggingAdapter log) {
    Integer opcode = packet.getOpcode();
    log.debug("游戏服收到网络包，等待处理 -> {}", opcode);

    ImmutableMap<Integer, NetHandleSuite> suiteMap = state.getHandleSuiteMap();
    NetHandleSuite suite = suiteMap.get(opcode);

    NetPacketCodec codec = suite.getPacketCodec();
    Object protoObj = codec.decodePacket(_typeInternal, packet.getData());

    NetHandleContext ctx = new NetHandleContext(protoObj,
        state.getDbCacheRef(), entityRef, log, _strInternal, _dbCmdInvoker);

    GameNetHandler<?> handler = suite.getHandleMeta().handler();
    handler.onHandle(ctx);
  }

  private final Z1 _typeInternal;
  private final Jstr0 _strInternal;

  private final DbCmdInvoker _dbCmdInvoker;
}
