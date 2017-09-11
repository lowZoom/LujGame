package lujgame.game.server.database.bean;

public interface DatabaseMeta {

  Class<?> databaseType();

  DbMetaField[] fields();
}
