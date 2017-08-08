package lujgame.robot.robot.instance.logic;

import akka.event.LoggingAdapter;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigRenderOptions;
import io.netty.channel.ChannelHandlerContext;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lujgame.core.akka.schedule.ActorScheduler;
import lujgame.robot.netty.RobotNetPacket;
import lujgame.robot.robot.instance.RobotInstanceActor;
import lujgame.robot.robot.instance.RobotInstanceState;
import lujgame.robot.robot.spawn.logic.RobotConfigReader;
import lujgame.robot.robot.spawn.logic.RobotGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RobotBehaver {

  @Autowired
  public RobotBehaver(ActorScheduler actorScheduler, RobotConfigReader robotConfigReader) {
    _actorScheduler = actorScheduler;
    _robotConfigReader = robotConfigReader;
  }

  public void initBehave(RobotBehaveState state) {
    state.setBehaviorIndex(-1);
    state.setBehaviorConfig(null);
  }

  public void doBehave(RobotInstanceState iState, RobotInstanceActor instanceActor,
      LoggingAdapter log) {
    RobotBehaveState bState = iState.getBehaveState();
    int behaviorIndex = bState.getBehaviorIndex() + 1;
    bState.setBehaviorIndex(behaviorIndex);

    RobotGroup robotGroup = iState.getRobotGroup();
    Config robotCfg = robotGroup.getConfig();

    RobotConfigReader r = _robotConfigReader;
    List<? extends Config> behaviorList = r.getBehaviorList(robotCfg);

    if (behaviorIndex >= behaviorList.size()) {
      log.info("机器人行为结束");
      return;
    }

    ChannelHandlerContext nettyContext = iState.getNettyContext();
    Config behaviorCfg = behaviorList.get(behaviorIndex);
    RobotNetPacket packet = encodePacket(behaviorCfg, log);

    log.debug("发送操作码 -> {}", packet.getOpcode());
    nettyContext.writeAndFlush(packet);

    long waitDur = r.getWaitDuration(behaviorCfg);
    _actorScheduler.schedule(instanceActor, waitDur,
        TimeUnit.MILLISECONDS, RobotInstanceActor.Behave.MSG);
  }

  private RobotNetPacket encodePacket(Config behaviorCfg, LoggingAdapter log) {
    final String KEY_DATA = "data";

    int opcode = behaviorCfg.getInt("op");
    String dataStr;

    try {
      Config dataCfg = behaviorCfg.getConfig(KEY_DATA);
      dataStr = dataCfg.root().render(ConfigRenderOptions.concise());
    } catch (ConfigException.WrongType ignored) {
      dataStr = behaviorCfg.getString(KEY_DATA);
    }

    log.debug("包 ----> {}", dataStr);
    return new RobotNetPacket(opcode, dataStr.getBytes(StandardCharsets.UTF_8));
  }

  private final ActorScheduler _actorScheduler;

  private final RobotConfigReader _robotConfigReader;
}
