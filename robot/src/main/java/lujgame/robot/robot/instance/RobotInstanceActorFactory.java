package lujgame.robot.robot.instance;

import akka.actor.Props;
import akka.japi.Creator;
import io.netty.channel.EventLoopGroup;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lujgame.robot.robot.config.RobotTemplate;
import lujgame.robot.robot.instance.RobotInstanceActor.Case;
import lujgame.robot.robot.instance.control.RobotBehaveState;
import org.springframework.stereotype.Service;

@Service
public class RobotInstanceActorFactory {

  public Props props(RobotTemplate robotGroup, EventLoopGroup eventGroup) {
    RobotBehaveState bState = new RobotBehaveState(robotGroup.getBehaviorList());
    RobotInstanceState iState = new RobotInstanceState(robotGroup, eventGroup, bState);

    Creator<RobotInstanceActor> c = () -> new RobotInstanceActor(iState, _handlerMap);

    return Props.create(RobotInstanceActor.class, c);
  }

  @PostConstruct
  void init() {
    List<Case> list = _handlerList;
    _handlerList = null;

    _handlerMap = list.stream()
        .collect(Collectors.toMap(this::getMessageType, Function.identity()));
  }

  private Class<?> getMessageType(Case handler) {
    return (Class<?>) ((ParameterizedType)handler.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
  }

  private Map<Class<?>, Case> _handlerMap;

  @Inject
  private List<Case> _handlerList;
}
