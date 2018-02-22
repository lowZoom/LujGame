package lujgame.core.spring;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

public class BeanNameGen extends AnnotationBeanNameGenerator {

  @Override
  protected String buildDefaultBeanName(BeanDefinition definition) {
    return loadClass(definition.getBeanClassName())
        .map(this::getNameWithParent)
        .orElseGet(() -> super.buildDefaultBeanName(definition));
  }

  private Optional<Class<?>> loadClass(String className) {
    try {
      return Optional.of(Class.forName(className));
    } catch (ClassNotFoundException ignored) {
      return Optional.empty();
    }
  }

  private String getNameWithParent(Class<?> beanType) {
    if (!Objects.equals(beanType.getSuperclass(), Object.class)) {
      return null;
    }
    Class<?>[] interfaceList = beanType.getInterfaces();
    if (interfaceList.length != 1) {
      return null;
    }
    String nameWithOuter = getSimpleNameWithOuter(interfaceList[0]);
    String outerName = getOuterName(nameWithOuter);
    if (outerName == null) {
      return null;
    }
    return String.format("%s.%s", outerName, beanType.getSimpleName());
  }

  private String getSimpleNameWithOuter(Class<?> clazz) {
    String fullName = clazz.getName();
    List<String> nameList = ImmutableList.copyOf(fullName.split("\\."));
    return Iterables.getLast(nameList);
  }

  private String getOuterName(String name) {
    String[] nameList = name.split("\\$");
    if (nameList.length < 2) {
      return null;
    }
    return nameList[nameList.length - 2];
  }
}
