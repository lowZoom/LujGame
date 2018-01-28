package lujgame.robot.robot.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.typesafe.config.ConfigFactory;
import javax.inject.Inject;
import lujgame.robot.test.RobotTest;
import org.junit.Before;
import org.junit.Test;

public class BehaviorConfigParserTest extends RobotTest {

  @Inject
  BehaviorConfigParser _parser;

  String _rawConfig;

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void parse_没有wait字段() throws Exception {
    //-- Arrange --//
    _rawConfig = "{op=1,data=1001}";

    //-- Act --//
    BehaviorConfig result = parse();

    //-- Assert --//
    assertThat(result.getOpcode()).isEqualTo(1);
    assertThat(result.getData()).isEqualTo("1001");

    assertThat(result.getWaitOption()).isEqualTo(BehaviorConfig.Wait.TIME);
    assertThat(result.getWaitMilli()).isEqualTo(0);
  }

  @Test
  public void parse_wait时间() throws Exception {
    //-- Arrange --//
    _rawConfig = "{op=1,data=1001,wait=2.55s}";

    //-- Act --//
    BehaviorConfig result = parse();

    //-- Assert --//
    assertThat(result.getWaitOption()).isEqualTo(BehaviorConfig.Wait.TIME);
    assertThat(result.getWaitMilli()).isEqualTo(2550);
  }

  @Test
  public void parse_wait回包() throws Exception {
    //-- Arrange --//
    _rawConfig = "{op=1,data=1001,wait=rsp}";

    //-- Act --//
    BehaviorConfig result = parse();

    //-- Assert --//
    assertThat(result.getWaitOption()).isEqualTo(BehaviorConfig.Wait.RESPONSE);
    assertThat(result.getWaitMilli()).isEqualTo(-1);
  }

  BehaviorConfig parse() {
    return _parser.parse(ConfigFactory.parseString(_rawConfig));
  }
}
