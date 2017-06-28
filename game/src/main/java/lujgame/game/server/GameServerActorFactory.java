package lujgame.game.server;

import akka.actor.Props;
import akka.japi.Creator;
import com.typesafe.config.Config;
import org.springframework.stereotype.Component;

@Component
public class GameServerActorFactory {

  public Props props(Config gameCfg) {
    String serverId = gameCfg.getString("server-id");

    GameServerActorState state = new GameServerActorState(serverId);

    Creator<GameServerActor> c = () -> {
      return new GameServerActor(state);
    };
    return Props.create(GameServerActor.class, c);
  }
}
