package lujgame.robot;

import lujgame.robot.boot.RobotBoot;
import lujgame.robot.boot.RobotInjectConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class RobotMain {

  public static void main(String[] args) {
    RobotMain main = new RobotMain();

    try (AnnotationConfigApplicationContext appCtx = main.prepare()) {
      main.start(appCtx, args);
    }
  }

  private AnnotationConfigApplicationContext prepare() {
    return new AnnotationConfigApplicationContext(RobotInjectConfig.class);
  }

  private void start(AnnotationConfigApplicationContext appCtx, String[] args) {
    RobotBoot boot = appCtx.getBean(RobotBoot.class);
    boot.boot(args);
  }
}
