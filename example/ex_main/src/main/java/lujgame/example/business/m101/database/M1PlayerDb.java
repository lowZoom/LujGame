package lujgame.example.business.m101.database;

import lujgame.game.server.type.JStr;
import lujgame.game.server.type.JTime;

public interface M1PlayerDb {

  JStr name();

  JTime loginTime();

  JTime logoutTime();

//  JMap<Integer, Long> attrMap();
}
