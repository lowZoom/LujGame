package lujgame.test;

import static org.mockito.Mockito.reset;

import java.lang.reflect.Field;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.util.ReflectionUtils;

@RunWith(SpringRunner.class)
@TestExecutionListeners(listeners = {
    DependencyInjectionTestExecutionListener.class,
//    MockitoTestExecutionListener.class,
})
public abstract class AbstractTest {

  @Before
  public void setup0() {
    // 利用多态取到实际的子类
    ReflectionUtils.doWithFields(getClass(), this::resetMock);
  }

  private void resetMock(Field f) throws IllegalAccessException {
    if (f.getModifiers() != 0 || f.getAnnotation(InjectMock.class) == null) {
      return;
    }
    f.setAccessible(true);
    reset(f.get(this));
  }
}
