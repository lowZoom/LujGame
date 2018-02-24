package lujgame.game.server.database.handle;

import lujgame.game.server.net.packet.NetPacket;
import lujgame.game.server.type.JStr;

@NetPacket
public interface ZTestPacket {

  JStr str();
}
