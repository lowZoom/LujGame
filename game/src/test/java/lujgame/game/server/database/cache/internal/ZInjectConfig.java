package lujgame.game.server.database.cache.internal;

import static org.mockito.Mockito.mock;

import lujgame.core.akka.AkkaTool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(
    value = "lujgame.game.server.database.cache",
    lazyInit = true
)
class ZInjectConfig {

  @Bean
  AkkaTool akkaTool() {
    return mock(AkkaTool.class);
  }
}
