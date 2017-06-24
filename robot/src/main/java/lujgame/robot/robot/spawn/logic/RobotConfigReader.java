package lujgame.robot.robot.spawn.logic;

import com.typesafe.config.Config;
import org.springframework.stereotype.Component;

@Component
public class RobotConfigReader {

  public String getIp(Config cfg) {
    return cfg.getString("ip");
  }

  public int getPort(Config cfg) {
    return cfg.getInt("port");
  }

  /**
   * 需要创建的机器人数量
   */
  public int getAmount(Config cfg) {
    return cfg.getInt("num");
  }
}
