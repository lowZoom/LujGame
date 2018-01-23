package lujgame.robot.robot.instance.logic;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;
import javax.inject.Inject;
import lujgame.robot.test.RobotTest;
import org.junit.Before;
import org.junit.Test;

public class RobotBehaverTest extends RobotTest {

  @Inject
  RobotBehaver _behaver;

  RobotBehaveState _robotBehaveState;

  @Before
  public void setUp() throws Exception {
    _robotBehaveState = new RobotBehaveState(ImmutableList.of());
  }

  @Test
  public void doBehave_() throws Exception {
    //-- Arrange --//
    _robotBehaveState.setBehaviorIndex(1);

    //-- Act --//
    doBehave();

    //-- Assert --//
    assertThat(_robotBehaveState.getBehaviorIndex()).isEqualTo(2);
  }

  void doBehave() {
//    _behaver.doBehave(_robotBehaveState, null, mock(LoggingAdapter.class));
  }
}
