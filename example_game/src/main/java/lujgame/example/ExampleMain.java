package lujgame.example;

import lujgame.game.facade.LujGame;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("lujgame.example")
public class ExampleMain {

  public static void main(String[] args) {
    new ExampleMain().start(args);
  }

  private void start(String[] args) {
    LujGame.quickStart(args, ExampleMain.class);
  }
}
