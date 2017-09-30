package lujgame.game.server.database.cache.internal;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.After;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ZInjectConfig.class)
public abstract class ZBaseTest {

  @After
  public void tearDown() throws IllegalAccessException {
    Class<? extends ZBaseTest> clazz = getClass();

    List<Field> mockList = Arrays.stream(clazz.getDeclaredFields())
        .filter(f -> f.getModifiers() == 0)
        .filter(f -> f.getAnnotation(Mock.class) != null)
        .collect(Collectors.toList());

    for (Field f : mockList) {
      Mockito.reset(f.get(this));
    }
  }
}
