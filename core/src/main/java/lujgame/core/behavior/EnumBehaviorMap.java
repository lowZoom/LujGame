package lujgame.core.behavior;

import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.ImmutableList;
import java.lang.reflect.ParameterizedType;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import lujgame.core.reflect.ReflectTool;
import lujgame.core.spring.SpringBeanCollector;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

public abstract class EnumBehaviorMap<E extends Enum<?>, B extends Behavior<?>> {

  public B getBehavior(E key) {
    return _behaviorMap.get(key);
  }

  @EventListener(ContextRefreshedEvent.class)
  void init() {
    ReflectTool r = _reflectTool;
    ParameterizedType mapType = r.getGenericSuperclass(getClass());

    Class<B> behaviorType = r.getTypeArgument(mapType, 1);
    List<E> enumList = getEnumList(mapType);

    _behaviorMap = _springBeanCollector.collectBeanMap(behaviorType, b -> makeKey(b, enumList));
  }

  private List<E> getEnumList(ParameterizedType mapType) {
    Class<E> enumType = _reflectTool.getTypeArgument(mapType, 0);
    E[] enumList = enumType.getEnumConstants();
    return ImmutableList.copyOf(enumList);
  }

  private E makeKey(B behavior, List<E> enumList) {
    String enumId = getEnumId(behavior);
    return enumList.stream()
        .filter(e -> maybeMatch(e, enumId))
        .min(Comparator.comparing(e -> e.name().length()))
        .orElse(null);
  }

  private String getEnumId(B behavior) {
    String behaviorName = behavior.getClass().getSimpleName();
    Matcher matcher = ID_PTN.matcher(behaviorName);
    checkState(matcher.matches(), behaviorName);
    return matcher.group(2);
  }

  private boolean maybeMatch(E enumObj, String enumId) {
    return enumObj.name().replaceAll("_", "").startsWith(enumId);
  }

  private static final Pattern ID_PTN = Pattern.compile("([A-Z][A-z]*)+?([A-Z]+)");

  private Map<E, B> _behaviorMap;

  @Inject
  private ReflectTool _reflectTool;

  @Inject
  private SpringBeanCollector _springBeanCollector;
}
