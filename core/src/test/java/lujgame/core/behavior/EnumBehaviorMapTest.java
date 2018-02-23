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
  }

  @Test
  public void getBehavior_() throws Exception {
    //-- Arrange --//
    _key = TestEnum.TEST;

    //-- Act --//
    TestBehavior result = getBehavior();

    //-- Assert --//
    assertThat(result).isInstanceOf(TestBehavT.class);
  }

  TestBehavior getBehavior() {
    return _map.getBehavior(_key);
  }
}
