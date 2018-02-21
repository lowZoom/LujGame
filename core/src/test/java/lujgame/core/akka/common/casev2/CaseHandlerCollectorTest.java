package lujgame.core.akka.common.casev2;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import javax.inject.Inject;
import lujgame.core.test.CoreTest;
import org.junit.Before;
import org.junit.Test;

public class CaseHandlerCollectorTest extends CoreTest {

  @Inject
  CaseHandlerCollector _collector;

  Class<TestCaseHandler> _handlerType;

  @Before
  public void setUp() throws Exception {
    _handlerType = TestCaseHandler.class;
  }

  @Test
  public void collect_() throws Exception {
    //-- Arrange --//

    //-- Act --//
    Map<Class<?>, TestCaseHandler> result = collect();

    //-- Assert --//
    assertThat(result.get(TestCaseHandler.Msg.class)).isNotNull();
    assertThat(result.get(TestDefaultCaseHandler.Msg.class)).isNotNull();
  }

  Map<Class<?>, TestCaseHandler> collect() {
    return _collector.collect(_handlerType);
  }
}
