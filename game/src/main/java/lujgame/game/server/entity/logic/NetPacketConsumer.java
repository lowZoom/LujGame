package lujgame.game.server.entity.logic;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import com.google.common.collect.ImmutableMap;
import lujgame.game.server.database.cache.internal.CacheKeyMaker;
import lujgame.game.server.entity.GameEntityActorState;
import lujgame.game.server.net.GameNetHandleContext;
import lujgame.game.server.net.GameNetHandler;
import lujgame.game.server.net.NetHandleSuite;
import lujgame.game.server.net.packet.NetPacketCodec;
import lujgame.game.server.net.internal.DbCmdInvoker;
import lujgame.game.server.type.Jstr0;
import lujgame.game.server.type.Z1;
import lujgame.gateway.network.akka.connection.logic.packet.GateNetPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NetPacketConsumer {

  @Autowired
  public NetPacketConsumer(
      Z1 typeInternal,
      Jstr0 strInternal,
      CacheKeyMaker cacheKeyMaker,
      DbCmdInvoker dbCmdInvoker) {
    _typeInternal = typeInternal;
    _strInternal = strInternal;

    _cacheKeyMaker = cacheKeyMaker;
    _dbCmdInvoker = dbCmdInvoker;
  }

  public void consumePacket(GameEntityActorState state, ActorRef entityRef,
      GateNetPacket packet, LoggingAdapter log) {
    Integer opcode = packet.getOpcode();
    log.debug("游戏服收到网络包，等待处理 -> {}", opcode);

    ImmutableMap<Integer, NetHandleSuite> suiteMap = state.getHandleSuiteMap();
    NetHandleSuite suite = suiteMap.get(opcode);

    NetPacketCodec codec = suite.getPacketCodec();
    Object protoObj = codec.decodePacket(_typeInternal, packet.getData());

    GameNetHandleContext ctx = new GameNetHandleContext(protoObj,
        state.getDbCacheRef(), entityRef, log, _strInternal, _cacheKeyMaker, _dbCmdInvoker);

    GameNetHandler<?> handler = suite.getHandleMeta().handler();
    handler.onHandle(ctx);
  }

  private final Z1 _typeInternal;
  private final Jstr0 _strInternal;

  private final CacheKeyMaker _cacheKeyMaker;
  private final DbCmdInvoker _dbCmdInvoker;
}
