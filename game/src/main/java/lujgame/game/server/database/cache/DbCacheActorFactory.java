package lujgame.game.server.database.cache;

import akka.actor.Props;
import akka.japi.Creator;
import com.typesafe.config.Config;
import org.springframework.stereotype.Component;

@Component
public class DbCacheActorFactory {

  public Props props(Config databaseCfg) {
    DbCacheActorState state = new DbCacheActorState(databaseCfg);

    Creator<DbCacheActor> c = () -> {
      return new DbCacheActor(state);
    };
    return Props.create(DbCacheActor.class, c);
  }
}
