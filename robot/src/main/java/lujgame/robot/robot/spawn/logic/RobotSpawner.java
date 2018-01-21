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
import javax.inject.Inject;
import lujgame.core.file.DataFilePathGetter;
import lujgame.core.file.FileTool;
import lujgame.robot.robot.config.RobotTemplate;
import lujgame.robot.robot.instance.RobotInstanceActorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class RobotSpawner {

  public void spawnRobot(List<RobotTemplate> groupList, EventLoopGroup eventGroup,
      UntypedActorContext actorCtx, LoggingAdapter log) {
    for (RobotTemplate group : groupList) {
      log.debug("启动机器人组 -> {}", group.getTemplateName());
      spawnGroup(group, eventGroup, actorCtx);
    }
  }

  private void spawnGroup(RobotTemplate robotGroup,
      EventLoopGroup eventGroup, UntypedActorContext actorCtx) {
    int num = robotGroup.getAmount();

    RobotInstanceActorFactory f = _robotInstanceFactory;
    for (int i = 0; i < num; i++) {
      Props props = f.props(robotGroup, eventGroup);
      ActorRef instanceRef = actorCtx.actorOf(props);
    }
  }

  @Inject
  private RobotInstanceActorFactory _robotInstanceFactory;
}
