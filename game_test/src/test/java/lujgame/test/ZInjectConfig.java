package lujgame.test;

import static org.mockito.Mockito.mock;

import lujgame.core.akka.AkkaTool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(
    basePackages = {
        "lujgame.core",
        "lujgame.anno",
        "lujgame.game.server"},
    lazyInit = true)
class ZInjectConfig {

  @Bean
  AkkaTool akkaTool() {
    return mock(AkkaTool.class);
  }
}
