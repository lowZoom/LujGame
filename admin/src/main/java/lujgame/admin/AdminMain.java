package lujgame.admin;

import lujgame.admin.boot.AdminBoot;
import lujgame.admin.boot.AdminInjectConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AdminMain {

  public static void main(String[] args) {
    try (AnnotationConfigApplicationContext appCtx =
        new AnnotationConfigApplicationContext(AdminInjectConfig.class)) {
      AdminBoot boot = appCtx.getBean(AdminBoot.class);
      boot.boot();
    }
  }
}
