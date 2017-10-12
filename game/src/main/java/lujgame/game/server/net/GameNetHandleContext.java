package lujgame.game.server.net;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister.Pack;
import com.sun.xml.internal.ws.api.message.Packet;
import lujgame.game.server.command.CacheOkCommand;
import lujgame.game.server.database.cache.internal.CacheKeyMaker;
import lujgame.game.server.database.cache.message.DbCacheUseItem;
import lujgame.game.server.database.cache.message.DbCacheUseReq;
import lujgame.game.server.type.JLong;
import lujgame.game.server.type.JStr;
import lujgame.game.server.type.Z1;
import org.omg.CORBA.NO_IMPLEMENT;

public class GameNetHandleContext {

  public GameNetHandleContext(
      Object proto,
      ActorRef dbCacheRef,
      ActorRef entityRef,
      LoggingAdapter log,
      Z1 typeI,
      CacheKeyMaker cacheKeyMaker) {
    _proto = proto;

    _dbCacheRef = dbCacheRef;
    _entityRef = entityRef;

    _log = log;

    _typeI = typeI;
    _cacheKeyMaker = cacheKeyMaker;

    _useList = ImmutableList.builder();
  }

  public <T> T getPacket(GameNetHandler<T> handler) {
    return (T) _proto;
  }

  public long get(JLong val) {
    throw new NO_IMPLEMENT("get尚未实现");
  }

  public String get(JStr val) {
    return _typeI.getImpl(val).getValue();
  }

  public void dbLoadSet(Class<?> dbType, JStr dbKey, String resultKey) {
    //TODO: 改成带在meta里
    String cacheKey = _cacheKeyMaker.makeSetKey(dbType, dbKey.toString());
    _useList.add(new DbCacheUseItem(cacheKey, dbType, dbKey.toString(), resultKey));
  }

  public <T extends CacheOkCommand> void invoke(Class<T> cmdType) {
    invoke(cmdType, null);
  }

  public <T extends CacheOkCommand> void invoke(Class<T> cmdType, Object packet) {
    ImmutableMap.Builder<String, Object> param = ImmutableMap.builder();
    if (packet != null) {
      param.put("packet", packet);
    }

    _dbCacheRef.tell(new DbCacheUseReq(_useList.build(), cmdType, param.build(), _entityRef, 0), _entityRef);
  }

  public LoggingAdapter log() {
    return _log;
  }

  private final Object _proto;

  //  private final Builder<DbCacheUseObjItem> _objUseList;
  private final ImmutableList.Builder<DbCacheUseItem> _useList;

  private final ActorRef _dbCacheRef;
  private final ActorRef _entityRef;

  private final LoggingAdapter _log;

  private final Z1 _typeI;
  private final CacheKeyMaker _cacheKeyMaker;
}
