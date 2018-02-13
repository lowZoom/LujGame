package lujgame.core.spring;

import java.lang.reflect.Modifier;
import java.util.Optional;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

public class BeanNameGen extends AnnotationBeanNameGenerator {

  @Override
  protected String buildDefaultBeanName(BeanDefinition definition) {
    return loadClass(definition.getBeanClassName())
        .filter(c -> !Modifier.isPublic(c.getModifiers()))
        .map(Class::getName)
        .orElseGet(() -> super.buildDefaultBeanName(definition));
  }

  private Optional<Class<?>> loadClass(String className) {
    try {
      return Optional.of(Class.forName(className));
    } catch (ClassNotFoundException ignored) {
      return Optional.empty();
    }
  }
}
