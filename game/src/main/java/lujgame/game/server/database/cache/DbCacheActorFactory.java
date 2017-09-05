package lujgame.game.server.database.cache;

import akka.actor.Props;
import akka.japi.Creator;
import org.springframework.stereotype.Component;

@Component
public class DbCacheActorFactory {

  public Props props() {
    Creator<DbCacheActor> c = () -> {
      return new DbCacheActor();
    };
    return Props.create(DbCacheActor.class, c);
  }
}
