package lujgame.game.facade;

import com.google.common.collect.ImmutableList;
import lujgame.game.boot.GameBoot;
import lujgame.game.boot.GameInjectConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class LujGame {

  public static void quickStart(String[] args, Class<?>... annotatedClasses) {
    try (AnnotationConfigApplicationContext appCtx =
        new AnnotationConfigApplicationContext(ImmutableList.<Class<?>>builder()
            .add(GameInjectConfig.class).addAll(ImmutableList.copyOf(annotatedClasses))
            .build().toArray(new Class<?>[annotatedClasses.length + 1]))) {
      GameBoot boot = appCtx.getBean(GameBoot.class);
      boot.boot(args);
    }
  }
}
