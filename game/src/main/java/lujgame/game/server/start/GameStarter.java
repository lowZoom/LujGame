package lujgame.game.server.start;

import com.google.common.collect.ImmutableMap;
import lujgame.core.spring.BeanCollector;
import lujgame.game.server.net.GameNetHandler;
import org.springframework.stereotype.Component;

@Component
public class GameStarter {

  public ImmutableMap<Integer, GameNetHandler> loadNetHandler(BeanCollector beanCollector) {
    return beanCollector.collectBeanMap(GameNetHandler.class, h -> {
      Class<? extends GameNetHandler> cls = h.getClass();
      String name = cls.getName();
      return Integer.valueOf(name.replace("Net1", ""));
    });
  }
}
