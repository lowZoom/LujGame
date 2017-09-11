package lujgame.game.server.database.io;

import com.zaxxer.hikari.HikariDataSource;

public class DbLoadActorState {

  public DbLoadActorState(HikariDataSource dataSource) {
    _dataSource = dataSource;
  }

  private final HikariDataSource _dataSource;
}
