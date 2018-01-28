package lujgame.robot.boot;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import javax.inject.Inject;
import lujgame.core.file.DataFileReader;
import lujgame.robot.boot.actor.RobotStartActorFactory;
import org.springframework.stereotype.Service;

@Service
public class RobotBoot {

  public void boot(String[] args) {
    Config systemCfg = _dataFileReader.readConfig("akka.conf");
    ActorSystem system = ActorSystem.create("Robot", systemCfg);

    Props startProps = _startActorFactory.props(null);
    system.actorOf(startProps, "Starter");
  }

  @Inject
  private DataFileReader _dataFileReader;

  @Inject
  private RobotStartActorFactory _startActorFactory;
}
