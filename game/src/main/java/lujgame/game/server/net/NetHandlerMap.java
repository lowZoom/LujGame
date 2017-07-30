package lujgame.game.server.net;

import com.google.common.collect.ImmutableMap;
import lujgame.core.spring.BeanCollector;
import lujgame.core.spring.BeanMap;

@BeanMap
public class NetHandlerMap {

  public NetHandlerMap(BeanCollector beanCollector) {
    _handlerMap = beanCollector.collectBeanMap(
        GameNetHandler.class, NetHandlerMap::getHandlerKey);
  }

  public GameNetHandler getNetHandler(Integer opcode) {
    return _handlerMap.get(opcode);
  }

  private static Integer getHandlerKey(GameNetHandler handler) {
    Class<? extends GameNetHandler> cls = handler.getClass();
    String name = cls.getName();
    return Integer.valueOf(name.replace("Net1", ""));
  }

  private final ImmutableMap<Integer, GameNetHandler> _handlerMap;
}
