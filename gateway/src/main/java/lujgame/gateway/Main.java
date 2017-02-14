package lujgame.gateway;

import lujgame.gateway.boot.GatewayBoot;
import lujgame.gateway.boot.GatewayInjectConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext appCtx
        = new AnnotationConfigApplicationContext(GatewayInjectConfig.class);

    GatewayBoot boot = appCtx.getBean(GatewayBoot.class);
    boot.boot();
  }
}
