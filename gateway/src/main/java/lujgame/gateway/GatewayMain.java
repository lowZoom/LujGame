package lujgame.gateway;

import lujgame.gateway.boot.GatewayBoot;
import lujgame.gateway.boot.GatewayInjectConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class GatewayMain {

  public static void main(String[] args) {
    new GatewayMain().start();
  }

  private void start() {
    //TODO: 接受控制台输入后考虑context close的问题
    AnnotationConfigApplicationContext appCtx =
        new AnnotationConfigApplicationContext(GatewayInjectConfig.class);

    GatewayBoot boot = appCtx.getBean(GatewayBoot.class);
    boot.boot();
  }
}
