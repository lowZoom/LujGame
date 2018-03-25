package lujgame.game.server.net.handle;

import akka.actor.ActorRef;
import com.google.common.collect.ImmutableList;
import lujgame.core.spring.inject.LujInternal;
import lujgame.game.server.database.cache.message.DbCacheUseItem;

@LujInternal
public class Nethandlecontext0 {

  public Integer getOpcode(NetHandleContext ctx) {
    return ctx._opcode;
  }

  public ImmutableList.Builder<DbCacheUseItem> getUseList(NetHandleContext ctx) {
    return ctx._useList;
  }

  public ActorRef getDbCacheRef(NetHandleContext ctx) {
    return ctx._dbCacheRef;
  }

  public ActorRef getEntityRef(NetHandleContext ctx) {
    return ctx._entityRef;
  }
}
