package lujgame.game.server;

import akka.actor.ActorRef;
import akka.cluster.Cluster;
import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;
import java.util.HashMap;
import java.util.Map;
import lujgame.game.server.command.CacheOkCommand;
import lujgame.game.server.database.bean.DatabaseMeta;
import lujgame.game.server.net.NetHandleSuite;

public class GameServerActorState {

  public GameServerActorState(String serverId, Config serverConfig, Cluster cluster,
      ImmutableMap<Integer, NetHandleSuite> handleSuiteMap,
      ImmutableMap<Class<?>, CacheOkCommand> cmdMap,
      ImmutableMap<Class<?>, DatabaseMeta> databaseMetaMap) {
    _serverId = serverId;
    _serverConfig = serverConfig;

    _cluster = cluster;

    _handleSuiteMap = handleSuiteMap;
    _cmdMap = cmdMap;

    _databaseMetaMap = databaseMetaMap;

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

  public ImmutableMap<Class<?>, CacheOkCommand> getCmdMap() {
    return _cmdMap;
  }

  public ImmutableMap<Class<?>, DatabaseMeta> getDatabaseMetaMap() {
    return _databaseMetaMap;
  }

  private ActorRef _dbCacheRef;

  private final Map<String, ActorRef> _entityMap;

  private final String _serverId;
  private final Config _serverConfig;

  private final Cluster _cluster;

  private final ImmutableMap<Integer, NetHandleSuite> _handleSuiteMap;
  private final ImmutableMap<Class<?>, CacheOkCommand> _cmdMap;

  private final ImmutableMap<Class<?>, DatabaseMeta> _databaseMetaMap;
}
