package lujgame.robot.robot.config;

import akka.event.LoggingAdapter;
import com.google.common.collect.ImmutableList;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lujgame.core.file.DataFilePathGetter;
import lujgame.core.file.FileTool;
import lujgame.robot.robot.config.RobotConfig.Abstract;
import org.springframework.stereotype.Service;

@Service
public class RobotConfigScanner {

  public List<RobotTemplate> scan(LoggingAdapter log) {

    List<Path> configPathList = findRobotConfig("robot", log);
    //FIXME: 直接parse

    Map<String, RobotConfig> configMap = configPathList.stream()
        .map(this::loadConfig)
        .collect(Collectors.toMap(RobotConfig::getName, Function.identity()));

    return configMap.values().stream()
        .filter(c -> c.getAbstract() == Abstract.NO)
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

  private Abstract getAbstract(Config cfg) {
    final String ABSTRACT = "abstract";
    if (!cfg.hasPath(ABSTRACT) || !cfg.getBoolean(ABSTRACT)) {
      return Abstract.NO;
    }
    return Abstract.YES;
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
    return new RobotConfig(name, cfg.withFallback(parentCfg), Abstract.NO, null);
  }

  private RobotTemplate toTemplate(RobotConfig config) {
    Config cfg = config.getConfig();

    List<BehaviorConfig> behaviorList = getBehaviorList(cfg).stream()
        .map(c -> parseBehavior(c))
        .collect(Collectors.toList());

    return new RobotTemplate(config.getName(), cfg.getString("ip"), cfg.getInt("port"),
        cfg.getInt("num"), behaviorList);
  }

  private List<? extends Config> getBehaviorList(Config cfg) {
    final String BEHAVIOR = "behavior";
    if (!cfg.hasPath(BEHAVIOR)) {
      return ImmutableList.of();
    }
    return cfg.getConfigList(BEHAVIOR);
  }

  private BehaviorConfig parseBehavior(Config behaviorCfg) {
    final String KEY_DATA = "data";

    int opcode = behaviorCfg.getInt("op");
    String dataStr;

    try {
      Config dataCfg = behaviorCfg.getConfig(KEY_DATA);
      dataStr = dataCfg.root().render(ConfigRenderOptions.concise());
    } catch (ConfigException.WrongType ignored) {
      dataStr = behaviorCfg.getString(KEY_DATA);
    }

    return new BehaviorConfig(opcode, dataStr);
  }

  private long getWaitDuration(Config behaviorCfg) {
    String key = "wait";
    return behaviorCfg.hasPath(key) ? behaviorCfg.getDuration(key, TimeUnit.MILLISECONDS) : 0;
  }

  @Inject
  private DataFilePathGetter _dataFilePathGetter;

  @Inject
  private FileTool _fileTool;
}
