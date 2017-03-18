package lujgame.robot.boot;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import lujgame.core.file.DataFileReader;
import lujgame.robot.boot.actor.RobotStartActorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RobotBoot {

  @Autowired
  public RobotBoot(
      DataFileReader dataFileReader,
      RobotStartActorFactory startActorFactory) {
    _dataFileReader = dataFileReader;
    _startActorFactory = startActorFactory;
  }

  public void boot(String[] args) {
    Config systemCfg = _dataFileReader.readConfig("akka.conf");
    ActorSystem system = ActorSystem.create("Robot", systemCfg);

    Props startProps = _startActorFactory.props();
    system.actorOf(startProps, "Starter");
  }

  private final DataFileReader _dataFileReader;
  private final RobotStartActorFactory _startActorFactory;
}
