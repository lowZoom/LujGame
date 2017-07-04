package lujgame.gateway.boot;

import com.typesafe.config.Config;
import lujgame.core.file.DataFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GatewayBootConfigLoader {

  @Autowired
  public GatewayBootConfigLoader(DataFileReader dataFileReader) {
    _dataFileReader = dataFileReader;
  }

  public Config loadGatewayConfig(String fileName) {
    return _dataFileReader.readConfig(fileName)
        .getConfig("gateway")
        .resolve();
  }

  public Config loadAkkaConfig(Config overrideCfg) {
    Config akkaCfg = _dataFileReader.readConfig("akka.conf");
    return overrideCfg.withFallback(akkaCfg);
  }

  private final DataFileReader _dataFileReader;
}
