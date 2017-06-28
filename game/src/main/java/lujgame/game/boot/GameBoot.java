package lujgame.game.boot;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import lujgame.core.file.DataFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameBoot {

  @Autowired
  public GameBoot(DataFileReader dataFileReader) {
    _dataFileReader = dataFileReader;
  }

  public void boot() {
    DataFileReader r = _dataFileReader;
    Config gameCfg = r.readConfig("game_seed.conf").getConfig("game").resolve();;

    Config akkaCfg = r.readConfig("game_seed_akka.conf");
    ActorSystem system = ActorSystem.create("Game", gameCfg
        .withFallback(akkaCfg));

    //TODO: 根据配置，如果有服务器ID，则启动游戏服务器，否则启动seed
  }

  private final DataFileReader _dataFileReader;
}
