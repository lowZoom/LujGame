package lujgame.test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ZInjectConfig.class)
@TestExecutionListeners(listeners = DependencyInjectionTestExecutionListener.class)
public abstract class ZBaseTest {

  @Before
  public void setUp0() throws IllegalAccessException {
    Class<? extends ZBaseTest> clazz = getClass();

    List<Field> mockList = Arrays.stream(clazz.getDeclaredFields())
        .filter(f -> f.getModifiers() == 0)
        .filter(f -> f.getAnnotation(Mock.class) != null)
        .collect(Collectors.toList());

    for (Field f : mockList) {
      f.setAccessible(true);
      Mockito.reset(f.get(this));
    }
  }
}
