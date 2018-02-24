package lujgame.game.server.database.handle;

import com.google.common.collect.ImmutableMap;
import lujgame.game.server.core.LujInternal;

/**
 * @see DbHandleContext
 */
@LujInternal
public class Dbhandlecontext0 {

  public ImmutableMap<String, Object> getResultMap(DbHandleContext ctx) {
    return ctx._resultMap;
  }
}
