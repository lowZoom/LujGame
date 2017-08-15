package lujgame.game.server.start;

import com.google.common.collect.ImmutableMap;
import lujgame.core.spring.BeanCollector;
import lujgame.game.server.net.NetHandleSuite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameStarter {

  @Autowired
  public GameStarter(BeanCollector beanCollector) {
    _beanCollector = beanCollector;
  }

  public ImmutableMap<Integer, NetHandleSuite> loadHandleSuiteMap() {
    return _beanCollector.collectBeanMap(NetHandleSuite.class, NetHandleSuite::getOpcode);
  }

  private final BeanCollector _beanCollector;
}
