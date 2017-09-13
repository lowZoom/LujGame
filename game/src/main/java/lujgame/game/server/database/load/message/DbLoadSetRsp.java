package lujgame.game.server.database.load.message;

import com.google.common.collect.ImmutableSet;

public class DbLoadSetRsp {

  public DbLoadSetRsp(ImmutableSet<Long> resultSet) {
    _resultSet = resultSet;
  }

  private final ImmutableSet<Long> _resultSet;
}
