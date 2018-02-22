package lujgame.core.spring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import lujgame.core.akka.common.casev2.TestCaseHandler;
import lujgame.core.akka.common.casev2.TestDefaultCaseHandler;
import lujgame.core.test.CoreTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;

public class BeanNameGenTest extends CoreTest {

  BeanNameGen _generator;

  Class<?> _beanType;

  @Before
  public void setUp() throws Exception {
    _generator = new BeanNameGen();
  }

  @Test
  public void buildDefaultBeanName_无任何继承实现() throws Exception {
    //-- Arrange --//
    _beanType = SpringBeanCollector.class;

    //-- Act --//
    String result = buildDefaultBeanName();

    //-- Assert --//
    assertThat(result).isEqualTo("springBeanCollector");
  }

  @Test
  public void buildDefaultBeanName_实现内部接口() throws Exception {
    //-- Arrange --//
    _beanType = TestCaseHandler.class;

    //-- Act --//
    String result = buildDefaultBeanName();

    //-- Assert --//
    assertThat(result).isEqualTo("TestActor.TestCaseHandler");
  }

  @Test
  public void buildDefaultBeanName_实现普通接口() throws Exception {
    //-- Arrange --//
    _beanType = TestDefaultCaseHandler.class;

    //-- Act --//
    String result = buildDefaultBeanName();

    //-- Assert --//
    assertThat(result).isEqualTo("testDefaultCaseHandler");
  }

  String buildDefaultBeanName() {
    BeanDefinition def = mock(BeanDefinition.class);
    when(def.getBeanClassName()).thenReturn(_beanType.getName());
    return _generator.buildDefaultBeanName(def);
  }
}
