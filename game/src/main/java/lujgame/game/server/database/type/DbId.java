package lujgame.game.server.database.type;

import java.util.LongSummaryStatistics;

public class DbId {

  public DbId(long val) {
    _val = val;
  }

  @Override
  public String toString() {
    return Long.toString(_val);
  }

  private final long _val;
}
