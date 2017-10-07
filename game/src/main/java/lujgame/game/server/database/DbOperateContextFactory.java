package lujgame.game.server.database;

import akka.actor.ActorRef;
import com.google.common.collect.ImmutableMap;
import lujgame.game.server.core.LujInternal;
import lujgame.game.server.database.bean.DatabaseMeta;
import lujgame.game.server.database.type.DbObjTool;
import lujgame.game.server.database.type.DbSetTool;
import org.springframework.beans.factory.annotation.Autowired;

@LujInternal
public class DbOperateContextFactory {

  public DbOperateContext createContext(
      ImmutableMap<String, Object> resultMap,
      ImmutableMap<Class<?>, DatabaseMeta> databaseMetaMap,
      ActorRef connRef) {
    return new DbOperateContext(resultMap, databaseMetaMap, connRef, _dbSetTool, _dbObjTool);
  }

  @Autowired
  private DbSetTool _dbSetTool;

  @Autowired
  private DbObjTool _dbObjTool;
}
