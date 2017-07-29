package lujgame.core.spring;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.stereotype.Component;

@Component
public class CollectResultMaker {

  public <K, T> ImmutableMap<K, T> makeMap(Collection<T> beans, Function<T, K> keyMapper) {
    Map<K, T> resultMap = new HashMap<>(beans.size());

    for (T bean : beans) {
      K id = keyMapper.apply(bean);
      checkNotNull(id, "键值不能为null！");

      T old = resultMap.put(id, bean);
      if (old != null) {
        throw new RuntimeException("键值重复注册："
            + id + ", " + old.getClass() + " <-> " + bean.getClass());
      }
    }

    return ImmutableMap.copyOf(resultMap);
  }
}
