package lujgame.game.server.database.cache.message;

import akka.actor.ActorRef;
import com.google.common.collect.ImmutableList;

public class DbCacheUseReq {

  public DbCacheUseReq(
//      ImmutableList<DbCacheUseItem> objUseList,
      ImmutableList<DbCacheUseItem> setUseList,
      Class<?> cmdType,
      ActorRef requestRef,
      int requestTime) {
//    _objUseList = objUseList;
    _setUseList = setUseList;

    _cmdType = cmdType;

    _requestRef = requestRef;
    _requestTime = requestTime;
  }

  public ImmutableList<DbCacheUseItem> getSetUseList() {
    return _setUseList;
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

  //  private final ImmutableList<DbCacheUseItem> _objUseList;
  private final ImmutableList<DbCacheUseItem> _setUseList;

  private final Class<?> _cmdType;

  private final ActorRef _requestRef;
  private final int _requestTime;
}
