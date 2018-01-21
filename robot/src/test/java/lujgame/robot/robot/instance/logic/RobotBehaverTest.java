package lujgame.robot.robot.instance.logic;

import static org.mockito.Mockito.mock;

import akka.event.LoggingAdapter;
import javax.inject.Inject;
import lujgame.robot.test.RobotTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

public class RobotBehaverTest extends RobotTest {

  @Inject
  RobotBehaver _behaver;

  RobotBehaveState _robotBehaveState;

  @Before
  public void setUp() throws Exception {
    _robotBehaveState = new RobotBehaveState(null);
  }

  @Test
  public void doBehave_() throws Exception {
    //-- Arrange --//

    //-- Act --//
    doBehave();

    //-- Assert --//

  }

  void doBehave() {
    _behaver.doBehave(_robotBehaveState, null, mock(LoggingAdapter.class));
  }
}
