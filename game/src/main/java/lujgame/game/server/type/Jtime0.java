package lujgame.game.server.type;

import lujgame.game.server.core.LujInternal;

@LujInternal
public class Jtime0 {

  public long getTime(JTime time) {
    return time._time;
  }

  public void setTime(JTime time, long val) {
    time._time = val;
  }
}
