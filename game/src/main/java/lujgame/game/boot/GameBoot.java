package lujgame.game.boot;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameBoot {

  @Autowired
  public GameBoot(GameBootConfigLoader bootConfigLoader) {
    _bootConfigLoader = bootConfigLoader;
  }

  public void boot(String[] args) {
    String fileName = args[0];

    GameBootConfigLoader l = _bootConfigLoader;
    Config gameCfg = l.loadGameConfig(fileName);

    if (l.isSeed(gameCfg)) {
      startSeed(gameCfg);
      return;
    }

    startGame(gameCfg);
  }

  private void startSeed(Config gameCfg) {
    Config akkaCfg = _bootConfigLoader.loadAkkaConfig(gameCfg);
    ActorSystem system = ActorSystem.create("Game", akkaCfg);

  }

  private void startGame(Config gameCfg) {
    GameBootConfigLoader l = _bootConfigLoader;
    Config akkaCfg = l.loadAkkaConfig(gameCfg);
    ActorSystem system = ActorSystem.create("Game", akkaCfg);

  }

  private final GameBootConfigLoader _bootConfigLoader;
}
