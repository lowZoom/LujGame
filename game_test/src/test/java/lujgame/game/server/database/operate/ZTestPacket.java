package lujgame.game.server.database.operate;

import lujgame.game.server.net.NetPacket;
import lujgame.game.server.type.JStr;

@NetPacket
public interface ZTestPacket {

  JStr str();
}
//
