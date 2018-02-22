package lujgame.robot.test;

import lujgame.core.spring.BeanNameGen;
import lujgame.test.AbstractTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = RobotTest.class)
@Configuration
@ComponentScan(lazyInit = true,
    nameGenerator = BeanNameGen.class,
    basePackages = {
        "lujgame.core",
        "lujgame.robot"
    })
public abstract class RobotTest extends AbstractTest {

}
