package lujgame.game.server.database.load;

import akka.actor.Props;
import akka.japi.Creator;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Component;

@Component
public class DbLoadActorFactory {

  public Props props(HikariDataSource dataSource) {
    DbLoadActorState state = new DbLoadActorState(dataSource);

    Creator<DbLoadActor> c = () -> {
      return new DbLoadActor(state);
    };

    return Props.create(DbLoadActor.class, c);
  }
}
