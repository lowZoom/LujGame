package lujgame.game.server.database.load;

import com.zaxxer.hikari.HikariDataSource;

public class DbLoadActorState {

  public DbLoadActorState(HikariDataSource dataSource) {
    _dataSource = dataSource;
  }

  public HikariDataSource getDataSource() {
    return _dataSource;
  }

  private final HikariDataSource _dataSource;
}
