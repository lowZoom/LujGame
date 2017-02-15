package lujgame.robot;

import lujgame.robot.boot.RobotBoot;
import lujgame.robot.boot.RobotInjectConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class RobotMain {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext appCtx
        = new AnnotationConfigApplicationContext(RobotInjectConfig.class);

    RobotBoot boot = appCtx.getBean(RobotBoot.class);
    boot.boot(args);
  }
}
