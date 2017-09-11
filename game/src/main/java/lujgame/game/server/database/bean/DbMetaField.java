package lujgame.game.server.database.bean;

public class DbMetaField {

  public DbMetaField(String name, Class<?> dbType) {
    _name = name;
    _dbType = dbType;
  }

  private final String _name;
  private final Class<?> _dbType;
}
