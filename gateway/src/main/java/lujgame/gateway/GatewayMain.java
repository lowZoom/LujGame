package lujgame.gateway;

import lujgame.gateway.boot.GatewayBoot;
import lujgame.gateway.boot.GatewayInjectConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class GatewayMain {

  public static void main(String[] args) {
    GatewayMain main = new GatewayMain();

    try (AnnotationConfigApplicationContext appCtx = main.prepare()) {
      main.start(appCtx);
    }
  }

  private AnnotationConfigApplicationContext prepare() {
    return new AnnotationConfigApplicationContext(GatewayInjectConfig.class);
  }

  private void start(AnnotationConfigApplicationContext appCtx) {
    GatewayBoot boot = appCtx.getBean(GatewayBoot.class);
    boot.boot();
  }
}
