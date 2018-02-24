package lujgame.game.server.database.handle;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import javax.inject.Inject;
import lujgame.game.server.core.LujInternal;
import lujgame.game.server.database.bean.DatabaseMeta;
import lujgame.game.server.database.cache.internal.CacheItem;
import lujgame.game.server.database.handle.internal.DbopNetTool;
import lujgame.game.server.database.type.DbObjTool;
import lujgame.game.server.database.type.DbSetTool;
import lujgame.game.server.net.packet.NetPacketCodec;
import lujgame.game.server.type.Jstr0;
import lujgame.game.server.type.Jtime0;

@LujInternal
public class DbHandleContextFactory {

  public DbHandleContext createContext(
      long now,
      ImmutableMap<String, Object> paramMap,
      ImmutableMap<String, Object> resultMap,
      ImmutableSet<CacheItem> borrowItems,
      ImmutableMap<Class<?>, DatabaseMeta> databaseMetaMap,
      ImmutableMap<Class<?>, NetPacketCodec> netPacketCodecMap,
      ActorRef connRef,
      ActorRef entityRef,
      LoggingAdapter log) {
    return new DbHandleContext(now, paramMap, resultMap, borrowItems, databaseMetaMap,
        netPacketCodecMap, connRef, entityRef, log, _dbSetTool, _dbObjTool, _dbopNetTool,
        _strInternal, _timeInternal);
  }

  @Inject
  private DbSetTool _dbSetTool;

  @Inject
  private DbObjTool _dbObjTool;

  @Inject
  private DbopNetTool _dbopNetTool;

  @Inject
  private Jstr0 _strInternal;

  @Inject
  private Jtime0 _timeInternal;
}
