package lujgame.core.spring;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.inject.Inject;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class SpringBeanCollector {

  public <K, T> ImmutableMap<K, T> collectBeanMap(Class<T> beanType, Function<T, K> keyMapper) {
    Collection<T> beans = _applicationContext.getBeansOfType(beanType).values();
    return makeMap(beans, keyMapper);
  }

  private <K, T> ImmutableMap<K, T> makeMap(Collection<T> beans, Function<T, K> keyMapper) {
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

  @Inject
  private ApplicationContext _applicationContext;
}
