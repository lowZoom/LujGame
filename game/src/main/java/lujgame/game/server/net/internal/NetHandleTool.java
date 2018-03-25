package lujgame.game.server.net.internal;

import com.google.common.collect.ImmutableList;
import javax.inject.Inject;
import lujgame.game.server.database.cache.internal.CacheKeyMaker;
import lujgame.game.server.database.cache.message.DbCacheUseItem;
import lujgame.game.server.database.handle.GameDbHandler;
import lujgame.game.server.database.handle.internal.DbHandleTool;
import lujgame.game.server.net.handle.NetHandleContext;
import lujgame.game.server.net.handle.Nethandlecontext0;
import lujgame.game.server.type.JStr;
import org.springframework.stereotype.Service;

@Service
public class NetHandleTool {

  public void dbLoadSet(NetHandleContext ctx, Class<?> dbType, JStr dbKey) {
    dbLoadSet(ctx, dbType, dbKey, DbHandleTool.SET_KEY);
  }

  public void dbLoadSet(NetHandleContext ctx, Class<?> dbType, JStr dbKey, String resultKey) {
    String dbKeyStr = dbKey.toString();
    String cacheKey = _cacheKeyMaker.makeSetKey(dbType, dbKeyStr);
    ImmutableList.Builder<DbCacheUseItem> useList = _contextInternal.getUseList(ctx);
    useList.add(new DbCacheUseItem(cacheKey, dbType, dbKeyStr, resultKey));
  }

  public <T extends GameDbHandler> void invoke(NetHandleContext ctx, Class<T> cmdType) {
    invoke(ctx, cmdType, null);
  }

  public <T extends GameDbHandler> void invoke(
      NetHandleContext ctx, Class<T> cmdType, Object packet) {
    Nethandlecontext0 i = _contextInternal;
    ImmutableList.Builder<DbCacheUseItem> useList = i.getUseList(ctx);

    _dbCmdInvoker.invoke(cmdType, i.getOpcode(ctx), packet,
        useList.build(), i.getDbCacheRef(ctx), i.getEntityRef(ctx));
  }

  @Inject
  private CacheKeyMaker _cacheKeyMaker;

  @Inject
  private Nethandlecontext0 _contextInternal;

  @Inject
  private DbCmdInvoker _dbCmdInvoker;
}
