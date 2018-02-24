package lujgame.game.server.net.handle;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import com.google.common.collect.ImmutableList;
import lujgame.game.server.database.cache.message.DbCacheUseItem;
import lujgame.game.server.database.handle.GameDbHandler;
import lujgame.game.server.net.internal.DbCmdInvoker;
import lujgame.game.server.type.JLong;
import lujgame.game.server.type.JStr;
import lujgame.game.server.type.Jstr0;
import org.omg.CORBA.NO_IMPLEMENT;

public class NetHandleContext {

  public NetHandleContext(
      Object proto,
      ActorRef dbCacheRef,
      ActorRef entityRef,
      LoggingAdapter log,
      Jstr0 strInternal,
      DbCmdInvoker dbCmdInvoker) {
    _proto = proto;

    _dbCacheRef = dbCacheRef;
    _entityRef = entityRef;

    _log = log;

    _strInternal = strInternal;
    _dbCmdInvoker = dbCmdInvoker;

    _useList = ImmutableList.builder();
  }

  @SuppressWarnings({"unchecked", "unused"})
  public <T> T getPacket(GameNetHandler<T> handler) {
    return (T) _proto;
  }

  public long get(JLong val) {
    throw new NO_IMPLEMENT("get尚未实现");
  }

  public String get(JStr val) {
    return _strInternal.getImpl(val).getValue();
  }

  public <T extends GameDbHandler> void invoke(Class<T> cmdType) {
    invoke(cmdType, null);
  }

  public <T extends GameDbHandler> void invoke(Class<T> cmdType, Object packet) {
    _dbCmdInvoker.invoke(cmdType, packet, _useList.build(), _dbCacheRef, _entityRef);
  }

  public LoggingAdapter log() {
    return _log;
  }

  private final Object _proto;

  //  private final Builder<DbCacheUseObjItem> _objUseList;
  final ImmutableList.Builder<DbCacheUseItem> _useList;

  private final ActorRef _dbCacheRef;
  private final ActorRef _entityRef;

  private final LoggingAdapter _log;

  private final Jstr0 _strInternal;

  private final DbCmdInvoker _dbCmdInvoker;
}
