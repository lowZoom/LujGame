package lujgame.game.boot;

import com.typesafe.config.Config;
import lujgame.core.file.DataFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameBootConfigLoader {

  @Autowired
  public GameBootConfigLoader(DataFileReader dataFileReader) {
    _dataFileReader = dataFileReader;
  }

  public Config loadGameConfig(String fileName) {
    final String ROOT_NAME = "game";

    DataFileReader r = _dataFileReader;
    Config defaultCfg = r.readConfig("game_default.conf").getConfig(ROOT_NAME);

    return r.readConfig(fileName)
        .getConfig(ROOT_NAME)
        .withFallback(defaultCfg)
        .resolve();
  }

  public Config loadAkkaConfig(Config overrideCfg) {
    Config akkaCfg = _dataFileReader.readConfig("game_akka.conf");
    return overrideCfg.withFallback(akkaCfg);
  }

  public String getServerId(Config gameCfg) {
    return gameCfg.getString("server-id");
  }

  public boolean isSeed(Config gameCfg) {
    return gameCfg.getBoolean("seed");
  }

  private final DataFileReader _dataFileReader;
}
