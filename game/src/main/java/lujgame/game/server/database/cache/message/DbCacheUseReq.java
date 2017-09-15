package lujgame.game.server.database.cache.message;

import akka.actor.ActorRef;
import com.google.common.collect.ImmutableList;
import java.util.function.BiConsumer;
import lujgame.game.server.database.DbOperateContext;

public class DbCacheUseReq {

  public DbCacheUseReq(
      ImmutableList<DbCacheUseItem> useList,
      Class<?> cmdType,
      ActorRef requestRef,
      int requestTime) {
    _useList = useList;
    _cmdType = cmdType;

    _requestRef = requestRef;
    _requestTime = requestTime;
  }

  public ImmutableList<DbCacheUseItem> getUseList() {
    return _useList;
  }

  public Class<?> getCmdType() {
    return _cmdType;
  }

  public ActorRef getRequestRef() {
    return _requestRef;
  }

  public int getRequestTime() {
    return _requestTime;
  }

  private final ImmutableList<DbCacheUseItem> _useList;
  private final Class<?> _cmdType;

  private final ActorRef _requestRef;
  private final int _requestTime;
}
