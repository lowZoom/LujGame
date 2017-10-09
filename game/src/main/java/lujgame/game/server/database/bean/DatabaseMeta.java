package lujgame.game.server.database.bean;

import lujgame.game.server.database.type.DbTypeInternal;

public interface DatabaseMeta {

  Class<?> databaseType();

//  DbMetaField[] fields();

  DbObjImpl createObject(DbTypeInternal i);
}
