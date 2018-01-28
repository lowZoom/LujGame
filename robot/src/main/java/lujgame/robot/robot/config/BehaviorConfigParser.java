package lujgame.robot.robot.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigRenderOptions;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;

@Service
public class BehaviorConfigParser {

  public BehaviorConfig parse(Config rawConfig) {
    int opcode = rawConfig.getInt("op");
    String dataKey = "data";
    String dataStr;

    try {
      Config dataCfg = rawConfig.getConfig(dataKey);
      dataStr = dataCfg.root().render(ConfigRenderOptions.concise());
    } catch (ConfigException.WrongType ignored) {
      dataStr = rawConfig.getString(dataKey);
    }

    String waitKey = "wait";
    if (!rawConfig.hasPath(waitKey)) {
      return new BehaviorConfig(opcode, dataStr, BehaviorConfig.Wait.TIME, 0);
    }

    try {
      long duration = rawConfig.getDuration(waitKey, TimeUnit.MILLISECONDS);
      return new BehaviorConfig(opcode, dataStr, BehaviorConfig.Wait.TIME, duration);
    } catch (ConfigException.BadValue ignored) {
      return new BehaviorConfig(opcode, dataStr, BehaviorConfig.Wait.RESPONSE, -1);
    }
  }
}
