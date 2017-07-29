package lujgame.core.spring;

import com.google.common.collect.ImmutableMap;
import java.util.Collection;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BeanCollector {

  @Autowired
  public BeanCollector(
      ApplicationContext applicationContext,
      CollectResultMaker collectResultMaker) {
    _applicationContext = applicationContext;
    _collectResultMaker = collectResultMaker;
  }

  public <K, T> ImmutableMap<K, T> collectBeanMap(Class<T> beanType, Function<T, K> keyMapper) {
    Collection<T> beans = _applicationContext.getBeansOfType(beanType).values();
    return _collectResultMaker.makeMap(beans, keyMapper);
  }

  private final ApplicationContext _applicationContext;
  private final CollectResultMaker _collectResultMaker;
}
