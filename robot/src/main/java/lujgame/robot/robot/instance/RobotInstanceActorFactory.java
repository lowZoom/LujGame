package lujgame.robot.robot.instance;

import akka.actor.Props;
import akka.japi.Creator;
import io.netty.channel.EventLoopGroup;
import org.springframework.stereotype.Component;

@Component
public class RobotInstanceActorFactory {

  public Props props(
      EventLoopGroup eventGroup,
      String ip,
      int port) {
    Creator<RobotInstanceActor> c = () -> new RobotInstanceActor(
        eventGroup,
        ip,
        port
    );
    return Props.create(RobotInstanceActor.class, c);
  }
}
