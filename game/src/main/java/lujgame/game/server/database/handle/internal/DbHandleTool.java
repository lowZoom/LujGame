package lujgame.game.server.database.handle.internal;

import com.google.common.collect.ImmutableMap;
import javax.inject.Inject;
import lujgame.game.server.database.handle.DbHandleContext;
import lujgame.game.server.database.handle.Dbhandlecontext0;
import lujgame.game.server.type.JSet;
import org.springframework.stereotype.Service;

@Service
public class DbHandleTool {

  public static final String SET_KEY = "Luj$DefaultDbSet";

  public <T> JSet<T> getDbSet(DbHandleContext ctx, Class<T> dbType) {
    return getDbSet(ctx, dbType, SET_KEY);
  }

  public <T> JSet<T> getDbSet(DbHandleContext ctx, Class<T> dbType, String key) {
    ImmutableMap<String, Object> resultMap = _contextInternal.getResultMap(ctx);
    return (JSet<T>) resultMap.get(key);
  }

  @Inject
  private Dbhandlecontext0 _contextInternal;
}
