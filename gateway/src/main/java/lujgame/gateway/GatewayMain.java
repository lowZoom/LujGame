package lujgame.gateway;

import lujgame.gateway.boot.GatewayBoot;
import lujgame.gateway.boot.GatewayInjectConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class GatewayMain {

  public static void main(String[] args) {
    new GatewayMain().start(args);
  }

  private void start(String[] args) {
    try (AnnotationConfigApplicationContext appCtx =
        new AnnotationConfigApplicationContext(GatewayInjectConfig.class)) {
      GatewayBoot boot = appCtx.getBean(GatewayBoot.class);
      boot.boot(args);
    }
  }
}
