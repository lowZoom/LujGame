package lujgame.robot.robot.spawn.logic;

import lujgame.core.file.FileTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RobotConfigReader {

  @Autowired
  public RobotConfigReader(FileTool fileTool) {
    _fileTool = fileTool;
  }

  private final FileTool _fileTool;
}
