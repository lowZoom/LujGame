package lujgame.example;

import lujgame.game.facade.LujGame;

public class ExampleMain {

  public static void main(String[] args) {
    new ExampleMain().start(args);
  }

  private void start(String[] args) {
    LujGame.quickStart(args);
  }
}
