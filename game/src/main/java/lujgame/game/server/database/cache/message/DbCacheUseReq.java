package lujgame.game.server.database.cache.message;

import akka.actor.ActorRef;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class DbCacheUseReq {

  public DbCacheUseReq(
//      ImmutableList<DbCacheUseItem> objUseList,
      ImmutableList<DbCacheUseItem> setUseList,
      Class<?> cmdType,
      ImmutableMap<String, Object> paramMap,
      ActorRef requestRef,
      int requestTime) {
//    _objUseList = objUseList;
    _setUseList = setUseList;

    _cmdType = cmdType;
    _paramMap = paramMap;

    _requestRef = requestRef;
    _requestTime = requestTime;
  }

  public ImmutableList<DbCacheUseItem> getSetUseList() {
    return _setUseList;
  }

  public Class<?> getCmdType() {
    return _cmdType;
  }

  public ImmutableMap<String, Object> getParamMap() {
    return _paramMap;
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
  private final ImmutableMap<String, Object> _paramMap;

  private final ActorRef _requestRef;
  private final int _requestTime;
}
