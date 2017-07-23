package lujgame.game.facade;

import lujgame.game.boot.GameBoot;
import lujgame.game.boot.GameInjectConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class LujGame {

  public static void quickStart(String[] args) {
    try (AnnotationConfigApplicationContext appCtx =
        new AnnotationConfigApplicationContext(GameInjectConfig.class)) {
      GameBoot boot = appCtx.getBean(GameBoot.class);
      boot.boot(args);
    }
  }
}
