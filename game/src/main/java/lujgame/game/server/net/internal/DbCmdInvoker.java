package lujgame.game.server.net.internal;

import akka.actor.ActorRef;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lujgame.core.akka.AkkaTool;
import lujgame.game.server.command.CacheOkCommand;
import lujgame.game.server.core.LujInternal;
import lujgame.game.server.database.cache.message.DbCacheUseItem;
import lujgame.game.server.database.cache.message.DbCacheUseReq;
import org.springframework.beans.factory.annotation.Autowired;

@LujInternal
public class DbCmdInvoker {

  /**
   * 请求调用指定类型的cmd
   */
  public <T extends CacheOkCommand> void invoke(Class<T> cmdType, Object packet,
      ImmutableList<DbCacheUseItem> setUseList, ActorRef dbCacheRef, ActorRef entityRef) {
    ImmutableMap.Builder<String, Object> param = ImmutableMap.builder();
    if (packet != null) {
      param.put("packet", packet);
    }

    _akkaTool.tell(new DbCacheUseReq(setUseList, cmdType,
        param.build(), entityRef, 0), entityRef, dbCacheRef);
  }

  @Autowired
  private AkkaTool _akkaTool;
}
