package lujgame.game.server.database.cache.message;

import com.google.common.collect.ImmutableList;
import java.util.function.BiConsumer;
import lujgame.game.server.database.DbOperateContext;

public class DbCacheUseReq {

  public DbCacheUseReq(
      ImmutableList<UseItem> useList,
      Class<?> cmdType,
      BiConsumer<?, DbOperateContext> cmdRunner) {
    _useList = useList;

    _cmdType = cmdType;
    _cmdRunner = cmdRunner;
  }

  public ImmutableList<UseItem> getUseList() {
    return _useList;
  }

  public Class<?> getCmdType() {
    return _cmdType;
  }

  public BiConsumer<?, DbOperateContext> getCmdRunner() {
    return _cmdRunner;
  }

  private final ImmutableList<UseItem> _useList;

  private final Class<?> _cmdType;
  private final BiConsumer<?, DbOperateContext> _cmdRunner;
}
