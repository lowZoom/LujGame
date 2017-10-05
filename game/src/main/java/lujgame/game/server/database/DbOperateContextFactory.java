package lujgame.game.server.database;

import com.google.common.collect.ImmutableMap;
import lujgame.game.server.core.LujInternal;
import lujgame.game.server.database.type.DbSetTool;
import org.springframework.beans.factory.annotation.Autowired;

@LujInternal
public class DbOperateContextFactory {

  public DbOperateContext createContext(ImmutableMap<String, Object> resultMap) {
    return new DbOperateContext(resultMap, _dbSetTool);
  }

  @Autowired
  private DbSetTool _dbSetTool;
}
