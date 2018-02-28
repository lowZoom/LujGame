package lujgame.game.server.database.handle;

import com.google.common.collect.ImmutableMap;
import lujgame.core.spring.inject.LujInternal;

/**
 * @see DbHandleContext
 */
@LujInternal
public class Dbhandlecontext0 {

  public ImmutableMap<String, Object> getParamMap(DbHandleContext ctx) {
    return ctx._paramMap;
  }

  public ImmutableMap<String, Object> getResultMap(DbHandleContext ctx) {
    return ctx._resultMap;
  }
}
