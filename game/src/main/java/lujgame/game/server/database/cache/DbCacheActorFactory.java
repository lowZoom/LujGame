package lujgame.game.server.database.cache;

import akka.actor.Props;
import akka.japi.Creator;
import com.typesafe.config.Config;
import lujgame.game.server.database.cache.internal.CacheUseSetFinisher;
import lujgame.game.server.database.cache.internal.CacheUseStarter;
import lujgame.game.server.database.load.DbLoadActorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DbCacheActorFactory {

  @Autowired
  public DbCacheActorFactory(
      DbLoadActorFactory dbLoadActorFactory,
      CacheUseStarter cacheUseStarter,
      CacheUseSetFinisher cacheUseSetFinisher) {
    _dbLoadActorFactory = dbLoadActorFactory;

    _cacheUseStarter = cacheUseStarter;
    _cacheUseSetFinisher = cacheUseSetFinisher;
  }

  public Props props(Config databaseCfg) {
    DbCacheActorState state = new DbCacheActorState(databaseCfg);

    Creator<DbCacheActor> c = () -> {
      return new DbCacheActor(state, _dbLoadActorFactory, _cacheUseStarter, _cacheUseSetFinisher);
    };
    return Props.create(DbCacheActor.class, c);
  }

  private final DbLoadActorFactory _dbLoadActorFactory;

  private final CacheUseStarter _cacheUseStarter;
  private final CacheUseSetFinisher _cacheUseSetFinisher;
}
