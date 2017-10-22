package lujgame.game.server.entity;

import lujgame.core.akka.common.CaseActor;
import lujgame.game.server.database.cache.message.DbCacheUseRsp;
import lujgame.game.server.entity.internal.DbCmdExecutor;
import lujgame.game.server.entity.internal.NetPacketConsumer;
import lujgame.gateway.network.akka.connection.message.Gate2GameMsg;

/**
 * 绑定一条连接
 */
public class GameEntityActor extends CaseActor {

  public GameEntityActor(
      GameEntityActorState state,
      NetPacketConsumer netPacketConsumer,
      DbCmdExecutor dbCmdExecutor) {
    _state = state;

    _netPacketConsumer = netPacketConsumer;
    _dbCmdExecutor = dbCmdExecutor;

    addCase(Gate2GameMsg.class, this::onNetPacket);
    addCase(DbCacheUseRsp.class, this::onDbCacheUseRsp);
  }

  /**
   * 处理网关服投递过来的包
   */
  private void onNetPacket(Gate2GameMsg msg) {
    _netPacketConsumer.consumePacket(_state, getSelf(), msg, log());
  }

  /**
   * 数据就绪后执行对应cmd
   */
  private void onDbCacheUseRsp(DbCacheUseRsp msg) {
    _dbCmdExecutor.executeCmd(_state, msg, getSelf(), System.currentTimeMillis());
  }

  private final GameEntityActorState _state;

  private final NetPacketConsumer _netPacketConsumer;
  private final DbCmdExecutor _dbCmdExecutor;
}
