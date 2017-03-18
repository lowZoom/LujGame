package lujgame.robot;

import lujgame.robot.boot.RobotBoot;
import lujgame.robot.boot.RobotInjectConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class RobotMain {

  public static void main(String[] args) {
    new RobotMain().start(args);
  }

  private void start(String[] args) {
    //TODO: 接受控制台输入后考虑context close的问题
    AnnotationConfigApplicationContext appCtx =
        new AnnotationConfigApplicationContext(RobotInjectConfig.class);

    RobotBoot boot = appCtx.getBean(RobotBoot.class);
    boot.boot(args);
  }
}
