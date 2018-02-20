package lujgame.core.test;

import lujgame.test.AbstractTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = CoreTest.class)
@Configuration
@ComponentScan(basePackages = {
    "lujgame.core",
}, lazyInit = true)
public class CoreTest extends AbstractTest {

}
