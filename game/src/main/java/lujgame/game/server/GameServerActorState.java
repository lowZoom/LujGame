package lujgame.game.server;

import akka.actor.ActorRef;
import akka.cluster.Cluster;
import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;
import java.util.HashMap;
import java.util.Map;
import lujgame.game.server.database.bean.DatabaseMeta;
import lujgame.game.server.database.handle.GameDbHandler;
import lujgame.game.server.net.handle.NetHandleSuite;
import lujgame.game.server.net.packet.NetPacketCodec;

public class GameServerActorState {

  public GameServerActorState(String serverId, Config serverConfig, Cluster cluster,
      ImmutableMap<Integer, NetHandleSuite> handleSuiteMap,
      ImmutableMap<Class<?>, GameDbHandler> cmdMap,
      ImmutableMap<Class<?>, DatabaseMeta> databaseMetaMap,
      ImmutableMap<Class<?>, NetPacketCodec> netPacketCodecMap) {
    _serverId = serverId;
    _serverConfig = serverConfig;

    _cluster = cluster;

    _handleSuiteMap = handleSuiteMap;
    _cmdMap = cmdMap;

    _databaseMetaMap = databaseMetaMap;
    _netPacketCodecMap = netPacketCodecMap;

    _entityMap = new HashMap<>(1024);
  }

  public ActorRef getDbCacheRef() {
    return _dbCacheRef;
  }

  public void setDbCacheRef(ActorRef dbCacheRef) {
    _dbCacheRef = dbCacheRef;
  }

  public Map<String, ActorRef> getEntityMap() {
    return _entityMap;
  }

  public String getServerId() {
    return _serverId;
  }

  public Config getServerConfig() {
    return _serverConfig;
  }

  public Cluster getCluster() {
    return _cluster;
  }

  public ImmutableMap<Integer, NetHandleSuite> getHandleSuiteMap() {
    return _handleSuiteMap;
  }

  public ImmutableMap<Class<?>, GameDbHandler> getCmdMap() {
    return _cmdMap;
  }

  public ImmutableMap<Class<?>, DatabaseMeta> getDatabaseMetaMap() {
    return _databaseMetaMap;
  }

  public ImmutableMap<Class<?>, NetPacketCodec> getNetPacketCodecMap() {
    return _netPacketCodecMap;
  }

  private ActorRef _dbCacheRef;

  private final Map<String, ActorRef> _entityMap;

  private final String _serverId;
  private final Config _serverConfig;

  private final Cluster _cluster;

  private final ImmutableMap<Integer, NetHandleSuite> _handleSuiteMap;
  private final ImmutableMap<Class<?>, GameDbHandler> _cmdMap;

  private final ImmutableMap<Class<?>, DatabaseMeta> _databaseMetaMap;
  private final ImmutableMap<Class<?>, NetPacketCodec> _netPacketCodecMap;
}
