package lujgame.robot.robot.config;

import java.util.List;

/**
 * 机器人创建模板
 */
public class RobotTemplate {

  public RobotTemplate(String templateName, String hostname, int port,
      int amount, List<BehaviorConfig> behaviorList) {
    _templateName = templateName;

    _hostname = hostname;
    _port = port;

    _amount = amount;
    _behaviorList = behaviorList;
  }

  public String getTemplateName() {
    return _templateName;
  }

  public String getHostname() {
    return _hostname;
  }

  public int getPort() {
    return _port;
  }

  public int getAmount() {
    return _amount;
  }

  public List<BehaviorConfig> getBehaviorList() {
    return _behaviorList;
  }

  private final String _templateName;

  private final String _hostname;
  private final int _port;

  private final int _amount;
  private final List<BehaviorConfig> _behaviorList;
}
