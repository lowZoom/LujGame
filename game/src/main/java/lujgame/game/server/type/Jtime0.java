package lujgame.game.server.type;

import lujgame.core.spring.inject.LujInternal;

@LujInternal
public class Jtime0 {

  public JTime newDb() {
    return new JTime();
  }

  public long getTime(JTime time) {
    return time._time;
  }

  public void setTime(JTime time, long val) {
    time._time = val;
  }
}
