package lujgame.robot.robot.config;


import com.typesafe.config.Config;

public class RobotConfig {

  public enum Abstract {
    YES, NO,
  }

  public RobotConfig(String name, Config config, Abstract anAbstract, String anExtends) {
    _name = name;
    _config = config;

    _abstract = anAbstract;
    _extends = anExtends;
  }

  public String getName() {
    return _name;
  }

  public Config getConfig() {
    return _config;
  }

  public Abstract getAbstract() {
    return _abstract;
  }

  public String getExtends() {
    return _extends;
  }

  private final String _name;
  private final Config _config;

  private final Abstract _abstract;
  private final String _extends;
}
