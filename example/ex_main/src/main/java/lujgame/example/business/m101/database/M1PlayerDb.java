package lujgame.example.business.m101.database;

import lujgame.game.server.database.bean.Database;
import lujgame.game.server.type.JStr;
import lujgame.game.server.type.JTime;

@Database
public interface M1PlayerDb {

  JStr name();

  JTime loginTime();

  JTime logoutTime();

//  JMap<Integer, Long> attrMap();
}
