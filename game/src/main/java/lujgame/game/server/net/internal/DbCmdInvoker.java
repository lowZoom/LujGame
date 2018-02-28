package lujgame.game.server.net.internal;

import akka.actor.ActorRef;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import javax.inject.Inject;
import lujgame.core.akka.AkkaTool;
import lujgame.core.spring.inject.LujInternal;
import lujgame.game.server.database.cache.message.DbCacheUseItem;
import lujgame.game.server.database.cache.message.DbCacheUseReq;
import lujgame.game.server.database.handle.GameDbHandler;

@LujInternal
public class DbCmdInvoker {

  /**
   * 请求调用指定类型的cmd
   */
  public <T extends GameDbHandler> void invoke(Class<T> cmdType, Object packet,
      ImmutableList<DbCacheUseItem> setUseList, ActorRef dbCacheRef, ActorRef entityRef) {
    ImmutableMap.Builder<String, Object> param = ImmutableMap.builder();
    if (packet != null) {
      param.put("packet", packet);
    }

    _akkaTool.tell(new DbCacheUseReq(setUseList, cmdType,
        param.build(), entityRef, 0), entityRef, dbCacheRef);
  }

  @Inject
  private AkkaTool _akkaTool;
}
