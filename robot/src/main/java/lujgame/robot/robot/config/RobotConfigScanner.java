package lujgame.robot.robot.config;

import akka.event.LoggingAdapter;
import com.google.common.collect.ImmutableList;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lujgame.core.file.DataFilePathGetter;
import lujgame.core.file.FileTool;
import org.springframework.stereotype.Service;

@Service
public class RobotConfigScanner {

  public List<RobotTemplate> scan(LoggingAdapter log) {
    Map<String, RobotConfig> configMap = findRobotConfig("robot", log).stream()
        .map(this::loadConfig)
        .collect(Collectors.toMap(RobotConfig::getName, Function.identity()));

    return configMap.values().stream()
        .filter(c -> c.getAbstract() == RobotConfig.Abstract.NO)
        .map(c -> expandGroup(c, configMap, log))
        .filter(Objects::nonNull)
        .map(this::toTemplate)
        .collect(Collectors.toList());
  }

  private List<Path> findRobotConfig(String dirName, LoggingAdapter log) {
    Path dirPath = _dataFilePathGetter.getRootPath(dirName);
    FileTool f = _fileTool;

    List<Path> children = f.getChildren(dirPath, p -> f.isExtension(p, "conf"));
    if (children.isEmpty()) {
      log.info("未发现任何机器人配置 -> {}", dirPath);
      return ImmutableList.of();
    }

    for (Path path : children) {
      log.info("检测到机器人配置 -> {}", path);
    }

    return children;
  }

  private RobotConfig loadConfig(Path path) {
    String configName = _fileTool.getName(path.toString());
    Config config = ConfigFactory.parseFile(path.toFile());
    config = config.getConfig("robot");
    return new RobotConfig(configName, config, getAbstract(config), getExtends(config));
  }

  private RobotConfig.Abstract getAbstract(Config cfg) {
    final String ABSTRACT = "abstract";
    if (!cfg.hasPath(ABSTRACT) || !cfg.getBoolean(ABSTRACT)) {
      return RobotConfig.Abstract.NO;
    }
    return RobotConfig.Abstract.YES;
  }

  private String getExtends(Config cfg) {
    final String EXTENDS = "extends";
    if (!cfg.hasPath(EXTENDS)) {
      return null;
    }
    return cfg.getString(EXTENDS);
  }

  private RobotConfig expandGroup(RobotConfig group,
      Map<String, RobotConfig> templateMap, LoggingAdapter log) {
    String extend = group.getExtends();

    // 没有继承东西则原样返回
    if (extend == null) {
      return group;
    }

    String name = group.getName();
    RobotConfig parentGroup = templateMap.get(extend);
    if (parentGroup == null) {
      log.warning("<警告> 配置[{}]展开失败，找不到模板：{}", name, extend);
      return null;
    }

    log.info("展开机器人配置 -> {}", name);

    Config parentCfg = parentGroup.getConfig();
    Config cfg = group.getConfig();
    return new RobotConfig(name, cfg.withFallback(parentCfg), RobotConfig.Abstract.NO, null);
  }

  private RobotTemplate toTemplate(RobotConfig config) {
    Config cfg = config.getConfig();

    List<BehaviorConfig> behaviorList = getBehaviorList(cfg).stream()
        .map(_behaviorConfigParser::parse)
        .collect(Collectors.toList());

    return new RobotTemplate(config.getName(), cfg.getString("hostname"),
        cfg.getInt("port"), cfg.getInt("num"), behaviorList);
  }

  private List<? extends Config> getBehaviorList(Config cfg) {
    final String BEHAVIOR = "behavior";
    if (!cfg.hasPath(BEHAVIOR)) {
      return ImmutableList.of();
    }
    return cfg.getConfigList(BEHAVIOR);
  }

  @Inject
  private DataFilePathGetter _dataFilePathGetter;

  @Inject
  private FileTool _fileTool;

  @Inject
  private BehaviorConfigParser _behaviorConfigParser;
}
