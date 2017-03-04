package lujgame.robot.boot;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import lujgame.core.file.DataFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RobotBoot {

  @Autowired
  public RobotBoot(
      DataFileReader dataFileReader) {
    _dataFileReader = dataFileReader;
  }

  public void boot(String[] args) {
    Config systemCfg = _dataFileReader.readConfig("akka.conf");
    ActorSystem system = ActorSystem.create("Robot", systemCfg);


  }

  private final DataFileReader _dataFileReader;
}
