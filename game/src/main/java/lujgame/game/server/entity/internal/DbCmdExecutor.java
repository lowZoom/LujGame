package lujgame.game.server.entity.internal;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import java.util.Map;
import javax.inject.Inject;
import lujgame.core.akka.AkkaTool;
import lujgame.game.server.command.CacheOkCommand;
import lujgame.game.server.core.LujInternal;
import lujgame.game.server.database.cache.message.DbCacheReturnMsg;
import lujgame.game.server.database.cache.message.DbCacheUseRsp;
import lujgame.game.server.database.operate.DbOperateContextFactory;
import lujgame.game.server.entity.GameEntityActorState;
import org.springframework.beans.factory.annotation.Autowired;

@LujInternal
public class DbCmdExecutor {

  public void executeCmd(GameEntityActorState state, DbCacheUseRsp msg,
      ActorRef entityRef, long now, LoggingAdapter log) {
    Map<Class<?>, CacheOkCommand> cmdMap = state.getCmdMap();
    Class<?> cmdType = msg.getCmdType();

    CacheOkCommand cmd = cmdMap.get(cmdType);
    cmd.execute(_dbOperateContextFactory.createContext(now, msg.getParamMap(), msg.getResultMap(),
        msg.getBorrowItems(), state.getDatabaseMetaMap(), state.getNetPacketCodecMap(),
        state.getConnRef(), entityRef, log));

    //TODO: 将修改部分发起到IO线程

    //TODO: 将缓存项归还
    _akkaTool.tell(new DbCacheReturnMsg(msg.getBorrowItems()), entityRef, state.getDbCacheRef());
  }

  @Inject
  private AkkaTool _akkaTool;

  @Inject
  private DbOperateContextFactory _dbOperateContextFactory;
}
