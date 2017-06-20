package lujgame.robot.robot.spawn.logic;

import akka.event.LoggingAdapter;
import com.google.common.collect.ImmutableList;
import java.nio.file.Path;
import java.util.List;
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
      RobotInstanceActorFactory robotInstanceFactory) {
    _fileTool = fileTool;
    _dataFilePathGetter = dataFilePathGetter;

    _robotInstanceFactory = robotInstanceFactory;
  }

  public List<Path> findRobotConfig(String dirName, LoggingAdapter log) {
    Path dirPath = _dataFilePathGetter.getRootPath(dirName);
    FileTool f = _fileTool;
    List<Path> children = f.getChildren(dirPath, p -> f.isExtension(p, "conf"));

    if (children.isEmpty()) {
      log.info("未发现任何机器人组配置 -> {}", dirPath);
      return ImmutableList.of();
    }

    for (Path path : children) {
      log.info("检测到机器人配置 -> {}", path);
    }

    return children;
  }

  public void spawnRobot(List<Path> pathList, LoggingAdapter log) {
    RobotInstanceActorFactory f = _robotInstanceFactory;

    for (Path path : pathList) {
      RobotGroup group = f.readGroup(path);
      if (!f.validateGroup(group)) {
        log.warning("无效的机器人配置 -> {}", path);
      }

      log.debug("启动机器人组 -> {}", group.getName());
    }
  }

  private final FileTool _fileTool;
  private final DataFilePathGetter _dataFilePathGetter;

  private final RobotInstanceActorFactory _robotInstanceFactory;
}
