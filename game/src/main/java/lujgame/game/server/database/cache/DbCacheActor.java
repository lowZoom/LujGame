package lujgame.game.server.database.cache;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.LoggingAdapter;
import com.google.common.cache.CacheBuilder;
import com.typesafe.config.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import lujgame.core.akka.common.CaseActor;
import lujgame.game.boot.message.BootFailMsg;
import lujgame.game.server.database.cache.internal.CacheUseSetFinisher;
import lujgame.game.server.database.cache.internal.CacheUseStarter;
import lujgame.game.server.database.cache.internal.DbCacheReturner;
import lujgame.game.server.database.cache.message.DbCacheReturnMsg;
import lujgame.game.server.database.cache.message.DbCacheUseReq;
import lujgame.game.server.database.load.DbLoadActorFactory;
import lujgame.game.server.database.load.message.DbLoadObjRsp;
import lujgame.game.server.database.load.message.DbLoadSetRsp;

public class DbCacheActor extends CaseActor {

  public DbCacheActor(
      DbCacheActorState state,
      DbLoadActorFactory dbLoadActorFactory,
      CacheUseStarter cacheUseStarter,
      CacheUseSetFinisher cacheUseSetFinisher,
      DbCacheReturner dbCacheReturner) {
    _state = state;

    _dbLoadActorFactory = dbLoadActorFactory;

    _cacheUseSetFinisher = cacheUseSetFinisher;
    _cacheUseStarter = cacheUseStarter;

    _dbCacheReturner = dbCacheReturner;

    addCase(DbCacheUseReq.class, this::onDbCacheUse);
    addCase(DbLoadSetRsp.class, this::onLoadSetRsp);
  }

  @Override
  public void preStart() {
    LoggingAdapter log = log();

    try {
      log.debug("检测数据库...");
      HikariDataSource dataSrc = initDatabase(_state.getDatabaseConfig());

      startLoadActor(dataSrc);

    } catch (SQLException | RuntimeException e) {
      ActorRef rootActor = getContext().parent();
      rootActor.tell(new BootFailMsg(e.getMessage()), getSelf());
      return;
    }

    _state.setCache(CacheBuilder.newBuilder()
        .maximumSize(65536)
        .expireAfterAccess(90, TimeUnit.MINUTES)
        .expireAfterWrite(90, TimeUnit.MINUTES)
        .build());
  }

  private HikariDataSource initDatabase(Config dbCfg) throws SQLException {
    HikariConfig cfg = new HikariConfig();
    cfg.setJdbcUrl(dbCfg.getString("url"));

    HikariDataSource dataSource = new HikariDataSource(cfg);
    Connection conn = dataSource.getConnection();

    log().debug("数据库连通性：" + conn.isValid(3));

    //TODO: 顺便检查所有的表都在不在

    conn.close();

    return dataSource;
  }

  private void startLoadActor(HikariDataSource dataSource) {
    Props props = _dbLoadActorFactory.props(dataSource);

    ActorRef loadRef = getContext().actorOf(props);
    _state.setLoaderRef(loadRef);
  }

  private void onDbCacheUse(DbCacheUseReq msg) {
    _cacheUseStarter.startUseObject(_state, msg, getSelf(), log());
  }

  private void onLoadSetRsp(DbLoadSetRsp msg) {
    _cacheUseSetFinisher.finishUseSet(_state, msg, getSelf(), log());
  }

  private void onLoadObjRsp(DbLoadObjRsp msg) {
//    _cacheUseFinisher.finishUseObject(_state, msg);
  }

  private void onDbCacheReturn(DbCacheReturnMsg msg) {
    _dbCacheReturner.returnCache(msg.getBorrowItems(), _state.getLockMap(), _state.getCache());
  }

  private final DbCacheActorState _state;

  private final DbLoadActorFactory _dbLoadActorFactory;

  private final CacheUseStarter _cacheUseStarter;
  //  private final CacheUseFinisher _cacheUseFinisher;
  private final CacheUseSetFinisher _cacheUseSetFinisher;

  private final DbCacheReturner _dbCacheReturner;
}
