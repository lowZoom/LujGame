package lujgame.game.server.start;

import com.google.common.collect.ImmutableMap;
import lujgame.core.spring.BeanCollector;
import lujgame.game.server.net.GameNetHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameStarter {

  @Autowired
  public GameStarter(BeanCollector beanCollector) {
    _beanCollector = beanCollector;
  }

  public ImmutableMap<Integer, GameNetHandler> loadNetHandler() {
    return _beanCollector.collectBeanMap(GameNetHandler.class, h -> {
      Class<? extends GameNetHandler> cls = h.getClass();
      String name = cls.getSimpleName();
      return Integer.valueOf(name.replace("Net", ""));
    });
  }

  private final BeanCollector _beanCollector;
}
