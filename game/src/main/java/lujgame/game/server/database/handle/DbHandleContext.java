package lujgame.game.server.database.handle;

import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import javax.annotation.Nullable;
import lujgame.game.server.database.bean.DatabaseMeta;
import lujgame.game.server.database.cache.internal.CacheItem;
import lujgame.game.server.database.handle.internal.DbopNetTool;
import lujgame.game.server.database.type.DbObjTool;
import lujgame.game.server.database.type.DbSetTool;
import lujgame.game.server.net.packet.NetPacketCodec;
import lujgame.game.server.net.packet.PacketImpl;
import lujgame.game.server.type.JSet;
import lujgame.game.server.type.JStr;
import lujgame.game.server.type.JTime;
import lujgame.game.server.type.Jstr0;
import lujgame.game.server.type.Jtime0;
import org.omg.CORBA.NO_IMPLEMENT;

public class DbHandleContext {

  public DbHandleContext(
      long now,
      ImmutableMap<String, Object> paramMap,
      ImmutableMap<String, Object> resultMap,
      ImmutableSet<CacheItem> borrowItems,
      ImmutableMap<Class<?>, DatabaseMeta> databaseMetaMap,
      ImmutableMap<Class<?>, NetPacketCodec> netPacketCodecMap,
      ActorRef connRef,
      ActorRef entityRef,
      LoggingAdapter log,
      DbSetTool dbSetTool,
      DbObjTool dbObjTool,
      DbopNetTool dbopNetTool,
      Jstr0 strInternal,
      Jtime0 timeInternal) {
    _now = now;
    _paramMap = paramMap;

    _resultMap = resultMap;
    _borrowItems = borrowItems;

    _databaseMetaMap = databaseMetaMap;
    _netPacketCodecMap = netPacketCodecMap;

    _connRef = connRef;

    _entityRef = entityRef;
    _log = log;

    _dbSetTool = dbSetTool;
    _dbObjTool = dbObjTool;

    _dbopNetTool = dbopNetTool;

    _strInternal = strInternal;
    _timeInternal = timeInternal;
  }

  public LoggingAdapter log() {
    return _log;
  }

  public <T> T getRequestPacket(Class<T> packetType) {
    return (T) _paramMap.get("luj.packet");
  }

  @Nullable
  public <T> T getDb(Class<T> dbType, String key) {
    throw new NO_IMPLEMENT("getDb尚未实现");
  }

  public <T> T getDb(JSet<T> set) {
    return _dbSetTool.getOnlyElem(set);
  }

  public boolean isEmpty(JSet<?> set) {
    return _dbSetTool.isEmpty(set);
  }

  public <T> T createDb(Class<T> dbType, JSet<T> dbSet) {
    return _dbSetTool.createObjAndAdd(dbType, _databaseMetaMap, now(), dbSet);
  }

  public <T> T createProto(Class<T> protoType) {
    return (T) _dbopNetTool.createProto(_netPacketCodecMap, protoType);
  }

  public void jSet(JStr field, String value) {
    _strInternal.getImpl(field).setValue(value);
  }

  public void jSet(JStr from, JStr to) {
    jSet(from, _strInternal.getImpl(to).getValue());
  }

  public void jSet(JTime field, JTime value) {
    throw new NO_IMPLEMENT("copy尚未实现");
  }

  public void sendError2C() {
    //TODO: 走单独的包，可能直接在业务层实现会比较好
    throw new NO_IMPLEMENT("sendError2C尚未实现");
  }

  public void sendResponse2C(Object proto) {
    Integer opcode = (Integer) _paramMap.get("luj.opcode");
    _dbopNetTool.sendToClient(_connRef, opcode, (PacketImpl<?>) proto, _entityRef);
  }

  public JTime now() {
    Jtime0 i = _timeInternal;

    JTime db = i.newDb();
    i.setTime(db, _now);

    return db;
  }

  private final long _now;
  final ImmutableMap<String, Object> _paramMap;

  final ImmutableMap<String, Object> _resultMap;
  private final ImmutableSet<CacheItem> _borrowItems;

  private final ImmutableMap<Class<?>, DatabaseMeta> _databaseMetaMap;
  private final ImmutableMap<Class<?>, NetPacketCodec> _netPacketCodecMap;

  // 在网关服上
  private final ActorRef _connRef;

  private final ActorRef _entityRef;
  private final LoggingAdapter _log;

  private final DbSetTool _dbSetTool;
  private final DbObjTool _dbObjTool;

  private final DbopNetTool _dbopNetTool;

  private final Jstr0 _strInternal;
  private final Jtime0 _timeInternal;
}
