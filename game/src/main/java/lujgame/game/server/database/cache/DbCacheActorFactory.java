package lujgame.game.server.database.cache;

import akka.actor.Props;
import akka.japi.Creator;
import com.typesafe.config.Config;
import javax.inject.Inject;
import lujgame.game.server.database.cache.internal.CacheUseSetFinisher;
import lujgame.game.server.database.cache.internal.CacheUseStarter;
import lujgame.game.server.database.cache.internal.DbCacheReturner;
import lujgame.game.server.database.load.DbLoadActorFactory;
import org.springframework.stereotype.Component;

@Component
public class DbCacheActorFactory {

  public Props props(Config databaseCfg) {
    DbCacheActorState state = new DbCacheActorState(databaseCfg);

    Creator<DbCacheActor> c = () -> {
      return new DbCacheActor(state, _dbLoadActorFactory,
          _cacheUseStarter, _cacheUseSetFinisher, _dbCacheReturner);
    };
    return Props.create(DbCacheActor.class, c);
  }

  @Inject
  private DbLoadActorFactory _dbLoadActorFactory;

  @Inject
  private CacheUseStarter _cacheUseStarter;

  @Inject
  private CacheUseSetFinisher _cacheUseSetFinisher;

  @Inject
  private DbCacheReturner _dbCacheReturner;
}
