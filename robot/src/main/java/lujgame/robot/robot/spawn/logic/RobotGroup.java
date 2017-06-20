package lujgame.robot.robot.spawn.logic;

import com.typesafe.config.Config;

public class RobotGroup {

  public RobotGroup(String name, Config config) {
    _name = name;
    _config = config;
  }

  public String getName() {
    return _name;
  }

  public Config getConfig() {
    return _config;
  }

  private final String _name;
  private final Config _config;
}
