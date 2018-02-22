package lujgame.game.boot;

import lujgame.core.spring.BeanNameGen;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {
    "lujgame.core",
    "lujgame.game",
}, nameGenerator = BeanNameGen.class)
public class GameInjectConfig {

}
