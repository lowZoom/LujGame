package lujgame.robot.robot.spawn.logic;

import com.google.common.collect.ImmutableList;
import com.typesafe.config.Config;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

@Component
public class RobotConfigReader {

  public boolean isAbstract(Config cfg) {
    final String ABSTRACT = "abstract";
    if (!cfg.hasPath(ABSTRACT)) {
      return false;
    }
    return cfg.getBoolean(ABSTRACT);
  }

  public Optional<String> getExtends(Config cfg) {
    final String EXTENDS = "extends";
    if (!cfg.hasPath(EXTENDS)) {
      return Optional.empty();
    }
    return Optional.of(cfg.getString(EXTENDS));
  }

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

  public List<? extends Config> getBehaviorList(Config cfg) {
    final String BEHAVIOR = "behavior";
    if (!cfg.hasPath(BEHAVIOR)) {
      return ImmutableList.of();
    }
    return cfg.getConfigList(BEHAVIOR);
  }

  public long getWaitDuration(Config behaviorCfg) {
    final String KEY_WAIT = "wait";
    return behaviorCfg.hasPath(KEY_WAIT) ?
        behaviorCfg.getDuration(KEY_WAIT, TimeUnit.MILLISECONDS) : 0;
  }
}
