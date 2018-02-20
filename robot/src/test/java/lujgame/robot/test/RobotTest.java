package lujgame.robot.test;

import lujgame.test.AbstractTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = RobotTest.class)
@Configuration
@ComponentScan(basePackages = {
    "lujgame.core",
    "lujgame.robot",
}, lazyInit = true)
public abstract class RobotTest extends AbstractTest {

}
