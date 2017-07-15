package lujgame.core.random;

import java.util.Random;
import org.springframework.stereotype.Component;

/**
 * 随机数适配层
 */
@Component
class RandomAdapter {

  /**
   * @see Random#nextInt(int)
   */
  public int nextInt(int bound) {
    return RAND.nextInt(bound);
  }

  /**
   * @see Random#nextDouble()
   */
  public double nextDouble() {
    return RAND.nextDouble();
  }

  private static final Random RAND = new Random();
}
