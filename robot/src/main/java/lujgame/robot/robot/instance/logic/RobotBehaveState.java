package lujgame.robot.robot.instance.logic;

import com.typesafe.config.Config;

public class RobotBehaveState {

  public Config getBehaviorConfig() {
    return _behaviorConfig;
  }

  public void setBehaviorConfig(Config behaviorConfig) {
    _behaviorConfig = behaviorConfig;
  }

  public int getBehaviorIndex() {
    return _behaviorIndex;
  }

  public void setBehaviorIndex(int behaviorIndex) {
    _behaviorIndex = behaviorIndex;
  }

  private Config _behaviorConfig;

  private int _behaviorIndex;
}
