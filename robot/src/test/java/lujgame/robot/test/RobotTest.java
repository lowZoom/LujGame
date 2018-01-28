package lujgame.robot.test;

import static org.mockito.Mockito.reset;

import java.lang.reflect.Field;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.util.ReflectionUtils;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = RobotTest.class)
@TestExecutionListeners(listeners = {
    DependencyInjectionTestExecutionListener.class,
//    MockitoTestExecutionListener.class,
})
@Configuration
@ComponentScan(basePackages = {
    "lujgame.core",
    "lujgame.robot",
}, lazyInit = true)
public abstract class RobotTest {

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
