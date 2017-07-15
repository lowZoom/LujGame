package lujgame.core.random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 随机数工具类
 */
@Component
public class RandomTool {

  @Autowired
  public RandomTool(RandomAdapter rand) {
    _rand = rand;
  }

  public int randInt(int minIn, int maxEx) {
    int range = maxEx - minIn;
    if (range <= 1) {
      return minIn;
    }

    int rand = _rand.nextInt(range);
    return minIn + rand;
  }

  public int randIntBetween(int minIn, int maxIn) {
    return randInt(minIn, maxIn + 1);
  }

  public boolean determineProbability(double probability) {
    return _rand.nextDouble() < probability;
  }

  private final RandomAdapter _rand;
}
