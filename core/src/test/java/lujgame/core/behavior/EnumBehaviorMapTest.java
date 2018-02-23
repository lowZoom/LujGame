package lujgame.core.behavior;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;
import lujgame.core.test.CoreTest;
import org.junit.Before;
import org.junit.Test;

public class EnumBehaviorMapTest extends CoreTest {

  @Inject
  TestEnumBehavMap _map;

  TestEnum _key;

  @Before
  public void setUp() throws Exception {
    // NOOP
  }

  @Test
  public void getBehavior_存在() throws Exception {
    //-- Arrange --//
    _key = TestEnum.JAVA;

    //-- Act --//
    TestBehavior result = getBehavior();

    //-- Assert --//
    assertThat(result).isInstanceOf(TestBehavJ.class);
  }

  @Test
  public void getBehavior_不存在() throws Exception {
    //-- Arrange --//
    _key = TestEnum.ROCKS;

    //-- Act --//
    TestBehavior result = getBehavior();

    //-- Assert --//
    assertThat(result).isNull();
  }

  TestBehavior getBehavior() {
    return _map.getBehavior(_key);
  }
}
