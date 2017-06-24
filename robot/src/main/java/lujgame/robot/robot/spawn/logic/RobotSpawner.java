package lujgame.robot.robot.spawn.logic;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActorContext;
import akka.event.LoggingAdapter;
import com.google.common.collect.ImmutableList;
import com.typesafe.config.Config;
import io.netty.channel.EventLoopGroup;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lujgame.core.file.DataFilePathGetter;
import lujgame.core.file.FileTool;
import lujgame.robot.robot.instance.RobotInstanceActorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RobotSpawner {

  @Autowired
  public RobotSpawner(FileTool fileTool,
      DataFilePathGetter dataFilePathGetter,
      RobotGroupMaker robotGroupMaker,
      RobotInstanceActorFactory robotInstanceFactory,
      RobotConfigReader robotConfigReader) {
    _fileTool = fileTool;
    _dataFilePathGetter = dataFilePathGetter;

    _robotGroupMaker = robotGroupMaker;

    _robotInstanceFactory = robotInstanceFactory;
    _robotConfigReader = robotConfigReader;
  }

  public List<Path> findRobotConfig(String dirName, LoggingAdapter log) {
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

  public List<RobotGroup> makeRobotGroup(List<Path> configList, LoggingAdapter log) {
    RobotGroupMaker m = _robotGroupMaker;

    Map<String, RobotGroup> templateMap = new HashMap<>(configList.size());
    List<RobotGroup> groupList = new ArrayList<>(configList.size());

    for (Path path : configList) {
      RobotGroup group = _robotInstanceFactory.readGroup(path);
      m.classifyGroup(group, templateMap, groupList);
    }

    return groupList.stream()
        .map(group -> m.expandGroup(group, templateMap, log))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  public void spawnRobot(List<RobotGroup> groupList, EventLoopGroup eventGroup,
      UntypedActorContext actorCtx, LoggingAdapter log) {
    for (RobotGroup group : groupList) {
      log.debug("启动机器人组 -> {}", group.getName());
      spawnGroup(group, eventGroup, actorCtx);
    }
  }

  private void spawnGroup(RobotGroup robotGroup,
      EventLoopGroup eventGroup, UntypedActorContext actorCtx) {
    Config cfg = robotGroup.getConfig();
    int num = _robotConfigReader.getAmount(cfg);

    RobotInstanceActorFactory f = _robotInstanceFactory;
    for (int i = 0; i < num; i++) {
      Props props = f.props(robotGroup, eventGroup);
      ActorRef instanceRef = actorCtx.actorOf(props);
    }
  }

  private final FileTool _fileTool;
  private final DataFilePathGetter _dataFilePathGetter;

  private final RobotGroupMaker _robotGroupMaker;

  private final RobotInstanceActorFactory _robotInstanceFactory;
  private final RobotConfigReader _robotConfigReader;
}
