package lujgame.core.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import org.springframework.stereotype.Service;

@Service
public class ReflectTool {

  public ParameterizedType getGenericSuperclass(Class<?> type) {
    return (ParameterizedType) type.getGenericSuperclass();
  }

  public ParameterizedType getGenericInterface(Class<?> type) {
    return Arrays.stream(type.getGenericInterfaces())
        .filter(t -> t instanceof ParameterizedType)
        .map(t -> (ParameterizedType) t)
        .findAny()
        .orElse(null);
  }

  @SuppressWarnings("unchecked")
  public <T extends Type> T getTypeArgument(ParameterizedType type, int argIndex) {
    return (T) type.getActualTypeArguments()[argIndex];
  }
}
