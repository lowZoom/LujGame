package lujgame.game.server.entity;

import java.util.Map;
import lujgame.core.akka.common.CaseActor;
import lujgame.game.server.command.CacheOkCommand;
import lujgame.game.server.database.cache.message.DbCacheUseRsp;
import lujgame.game.server.database.operate.DbOperateContextFactory;
import lujgame.game.server.entity.logic.NetPacketConsumer;
import lujgame.gateway.network.akka.connection.message.Gate2GameMsg;

/**
 * 绑定一条连接
 */
public class GameEntityActor extends CaseActor {

  public GameEntityActor(
      GameEntityActorState state,
      NetPacketConsumer netPacketConsumer,
      DbOperateContextFactory dbOperateContextFactory) {
    _state = state;

    _netPacketConsumer = netPacketConsumer;
    _dbOperateContextFactory = dbOperateContextFactory;

    addCase(Gate2GameMsg.class, this::onNetPacket);
    addCase(DbCacheUseRsp.class, this::onDbCacheUseRsp);
  }

  /**
   * 处理网关服投递过来的包
   */
  private void onNetPacket(Gate2GameMsg msg) {
    _netPacketConsumer.consumePacket(_state, getSelf(), msg, log());
  }

  private void onDbCacheUseRsp(DbCacheUseRsp msg) {
    GameEntityActorState s = _state;
    Map<Class<?>, CacheOkCommand> cmdMap = s.getCmdMap();
    Class<?> cmdType = msg.getCmdType();

    CacheOkCommand cmd = cmdMap.get(cmdType);
    cmd.execute(_dbOperateContextFactory.createContext(System.currentTimeMillis(),
        msg.getParamMap(), msg.getResultMap(), s.getDatabaseMetaMap(),
        s.getNetPacketCodecMap(), s.getConnRef(), getSelf()));
  }

  private final GameEntityActorState _state;

  private final NetPacketConsumer _netPacketConsumer;
  private final DbOperateContextFactory _dbOperateContextFactory;
}
