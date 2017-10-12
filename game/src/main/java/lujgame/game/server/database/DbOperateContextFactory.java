package lujgame.game.server.database;

import akka.actor.ActorRef;
import com.google.common.collect.ImmutableMap;
import lujgame.game.server.core.LujInternal;
import lujgame.game.server.database.bean.DatabaseMeta;
import lujgame.game.server.database.type.DbObjTool;
import lujgame.game.server.database.type.DbSetTool;
import lujgame.game.server.type.Jtime0;
import org.springframework.beans.factory.annotation.Autowired;

@LujInternal
public class DbOperateContextFactory {

  public DbOperateContext createContext(
      long now,
      ImmutableMap<String, Object> paramMap,
      ImmutableMap<String, Object> resultMap,
      ImmutableMap<Class<?>, DatabaseMeta> databaseMetaMap,
      ActorRef connRef) {
    return new DbOperateContext(now, paramMap, resultMap, databaseMetaMap, connRef,
        _dbSetTool, _dbObjTool, _timeInternal);
  }

  @Autowired
  private DbSetTool _dbSetTool;

  @Autowired
  private DbObjTool _dbObjTool;

  @Autowired
  private Jtime0 _timeInternal;
}
