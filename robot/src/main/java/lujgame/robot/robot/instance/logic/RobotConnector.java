package lujgame.robot.robot.instance.logic;

import akka.event.LoggingAdapter;
import com.typesafe.config.Config;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.function.BiConsumer;
import lujgame.robot.robot.instance.RobotInstanceState;
import lujgame.robot.robot.spawn.logic.RobotConfigReader;
import lujgame.robot.robot.spawn.logic.RobotGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RobotConnector {

  @Autowired
  public RobotConnector(RobotConfigReader robotConfigReader) {
    _robotConfigReader = robotConfigReader;
  }

  public void startConnect(RobotInstanceState state,
      BiConsumer<Boolean, LoggingAdapter> listener, LoggingAdapter log) {
    RobotGroup robotGroup = state.getRobotGroup();
    Config robotCfg = robotGroup.getConfig();

    RobotConfigReader r = _robotConfigReader;
    String ip = r.getIp(robotCfg);
    int port = r.getPort(robotCfg);
    log.info("检测目标服务器 -> {}:{}", ip, port);

    //TODO: 只创一个，整个APP重用
    new Bootstrap()
        .group(state.getWorkerGroup())
        .channel(NioSocketChannel.class)
//        .option(ChannelOption.SO_KEEPALIVE, true)
        .handler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel socketChannel) throws Exception {

          }
        })
        .connect(ip, port)
        .addListener(f -> listener.accept(f.isSuccess(), log));
  }

  public void onConnectDone(boolean success, LoggingAdapter log) {
    log.debug("连接结果：{}", success);
  }

  private final RobotConfigReader _robotConfigReader;
}
