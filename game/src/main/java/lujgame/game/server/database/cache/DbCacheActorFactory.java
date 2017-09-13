package lujgame.game.server.database.cache;

import akka.actor.Props;
import akka.japi.Creator;
import com.typesafe.config.Config;
import lujgame.game.server.database.cache.internal.DbCacheUser;
import lujgame.game.server.database.load.DbLoadActorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DbCacheActorFactory {

  @Autowired
  public DbCacheActorFactory(
      DbLoadActorFactory dbLoadActorFactory,
      DbCacheUser dbCacheUser) {
    _dbLoadActorFactory = dbLoadActorFactory;
    _dbCacheUser = dbCacheUser;
  }

  public Props props(Config databaseCfg) {
    DbCacheActorState state = new DbCacheActorState(databaseCfg);

    Creator<DbCacheActor> c = () -> {
      return new DbCacheActor(state, _dbLoadActorFactory, _dbCacheUser);
    };
    return Props.create(DbCacheActor.class, c);
  }

  private final DbLoadActorFactory _dbLoadActorFactory;
  private final DbCacheUser _dbCacheUser;
}
