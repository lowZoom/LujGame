package lujgame.game.server.database.operate;

import akka.actor.ActorRef;
import com.google.common.collect.ImmutableMap;
import lujgame.game.server.core.LujInternal;
import lujgame.game.server.database.bean.DatabaseMeta;
import lujgame.game.server.database.operate.internal.DbopNetTool;
import lujgame.game.server.database.type.DbObjTool;
import lujgame.game.server.database.type.DbSetTool;
import lujgame.game.server.net.packet.NetPacketCodec;
import lujgame.game.server.type.Jstr0;
import lujgame.game.server.type.Jtime0;
import org.springframework.beans.factory.annotation.Autowired;

@LujInternal
public class DbOperateContextFactory {

  public DbOperateContext createContext(
      long now,
      ImmutableMap<String, Object> paramMap,
      ImmutableMap<String, Object> resultMap,
      ImmutableMap<Class<?>, DatabaseMeta> databaseMetaMap,
      ImmutableMap<Class<?>, NetPacketCodec> netPacketCodecMap,
      ActorRef connRef,
      ActorRef entityRef) {
    return new DbOperateContext(now, paramMap, resultMap, databaseMetaMap, netPacketCodecMap,
        connRef, entityRef, _dbSetTool, _dbObjTool, _dbopNetTool, _strInternal, _timeInternal);
  }

  @Autowired
  private DbSetTool _dbSetTool;

  @Autowired
  private DbObjTool _dbObjTool;

  @Autowired
  private DbopNetTool _dbopNetTool;

  @Autowired
  private Jstr0 _strInternal;

  @Autowired
  private Jtime0 _timeInternal;
}
