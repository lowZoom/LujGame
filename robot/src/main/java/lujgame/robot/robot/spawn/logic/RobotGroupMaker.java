package lujgame.robot.robot.spawn.logic;

import akka.event.LoggingAdapter;
import com.typesafe.config.Config;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RobotGroupMaker {

  @Autowired
  public RobotGroupMaker(RobotConfigReader robotConfigReader) {
    _robotConfigReader = robotConfigReader;
  }

  public void classifyGroup(RobotGroup group,
      Map<String, RobotGroup> templateMap, List<RobotGroup> groupList) {
    Config cfg = group.getConfig();
    boolean isAbstract = _robotConfigReader.isAbstract(cfg);

    if (isAbstract) {
      String name = group.getName();
      templateMap.put(name, group);
      return;
    }

    groupList.add(group);
  }

  public RobotGroup expandGroup(RobotGroup group,
      Map<String, RobotGroup> templateMap, LoggingAdapter log) {
    Config cfg = group.getConfig();
    Optional<String> extendR = _robotConfigReader.getExtends(cfg);

    // 没有继承东西则原样返回
    if (!extendR.isPresent()) {
      return group;
    }

    String name = group.getName();
    String extend = extendR.get();

    RobotGroup parentGroup = templateMap.get(extend);
    if (parentGroup == null) {
      log.warning("<警告> 配置[{}]展开失败，找不到模板：{}", name, extend);
      return null;
    }

    log.info("展开机器人配置 -> {}", name);

    Config parentCfg = parentGroup.getConfig();
    return new RobotGroup(name, cfg.withFallback(parentCfg));
  }

  private final RobotConfigReader _robotConfigReader;
}
