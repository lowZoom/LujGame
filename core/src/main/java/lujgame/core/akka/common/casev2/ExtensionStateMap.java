package lujgame.core.akka.common.casev2;

import java.util.HashMap;
import java.util.Map;

public class ExtensionStateMap {

  public ExtensionStateMap() {
    _stateMap = new HashMap<>(8);
  }

  public void put(Class<?> key, Object value) {
    _stateMap.put(key, value);
  }

  @SuppressWarnings("unchecked")
  public <T> T get(Class<?> key) {
    return (T) _stateMap.get(key);
  }

  private final Map<Class<?>, Object> _stateMap;
}
