package lujgame.example.business.m101.database;

import lujgame.game.server.type.JLong;
import lujgame.game.server.type.JMap;
import lujgame.game.server.type.JStr;

public interface M1PlayerDb {

  JStr name();

  JLong loginTime();

  JLong logoutTime();

  JMap<Integer, Long> attrMap();
}
