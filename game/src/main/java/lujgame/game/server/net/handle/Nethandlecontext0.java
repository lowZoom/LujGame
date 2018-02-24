package lujgame.game.server.net.handle;

import com.google.common.collect.ImmutableList;
import lujgame.game.server.core.LujInternal;
import lujgame.game.server.database.cache.message.DbCacheUseItem;

/**
 * @see NetHandleContext
 */
@LujInternal
public class Nethandlecontext0 {

  public ImmutableList.Builder<DbCacheUseItem> getUseList(NetHandleContext ctx) {
    return ctx._useList;
  }
}
